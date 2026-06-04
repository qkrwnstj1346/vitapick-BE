package com.vita.vitapickBack.users;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vita.vitapickBack.jwtToken.RefreshToken;
import com.vita.vitapickBack.jwtToken.RefreshTokenRepository;
import com.vita.vitapickBack.jwtToken.TokenProvider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService{

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refRepository;
    
    // 아이디 중복확인
    @Override
    public boolean checkLoginId(String loginId) {
        log.info("** checkLoginId => " + loginId);
        return usersRepository.existsByLoginId(loginId);
    }
    
    // 이메일 중복확인
    @Override
    public boolean checkEmail(String email) {
    	log.info("** checkEmail => " + email);
    	return usersRepository.existsByEmail(email);
    }
    
    // 회원가입
    @Override
    public void signup(UsersDTO usersDTO) {
        log.info("** signup => " + usersDTO.getLoginId());

        // 비밀번호 암호화
        usersDTO.setPwd(passwordEncoder.encode(usersDTO.getPwd()));

        // Entity 생성
        Users users = Users.builder()
            .loginId(usersDTO.getLoginId())
            .pwd(usersDTO.getPwd())
            .userNm(usersDTO.getUserNm())
            .tel(usersDTO.getTel())
            .email(usersDTO.getEmail())
            .genderCd(usersDTO.getGenderCd())
            .birthYmd(usersDTO.getBirthYmd())
            .statusCd("ACTIVE")    // 기본값 활성
            .roleCd("USER")   // 기본값 일반회원
            .build();

        usersRepository.save(users);
    }
    
    // 아이디찾기
    @Override
    public UsersDTO findId(UsersDTO usersDTO) {
    	Users users = usersRepository.findByUserNmAndEmail(usersDTO.getUserNm(), usersDTO.getEmail())
    									.orElseThrow(() -> new RuntimeException("일치하는 회원 정보가 없습니다."));
    	return UsersDTO.builder()
    			.loginId(users.getLoginId())
    			.build();
    }
    
    // 로그인
    @Override
    @Transactional
    public UsersDTO login(HttpServletResponse response, Users entity) {
    	
    	//1) 요청분석
    	String pwd = entity.getPwd();
    	String loginId = entity.getLoginId();
        log.info("** login => " + entity.getLoginId());
        log.info("** login pwd => " + entity.getPwd());
        
        //2) 서비스처리 & 결과전송
        try {
        	entity = usersRepository.findByLoginId(loginId).orElseThrow(()-> new RuntimeException("회원 없음"));
        	if(entity != null && passwordEncoder.matches(pwd, entity.getPwd())) {
        		final UsersDTO usersDTO = tokenProvider.generateToken(entity.claimList());
        		
        		log.info("로그인 성공=>" + HttpStatus.OK+entity.claimList());
        		
        		//=> RefreshToken DB에 저장 & 쿠키에 담아 전송
        		RefreshToken refreshToken = RefreshToken.builder()
        				.userNum(entity.getUserNum())
        				.loginId(entity.getLoginId())
        				.refreshToken(usersDTO.getRefreshToken())
        				.expiration(usersDTO.getRefreshTokenExpiresln())
        				.build();
        		refRepository.save((refreshToken));
        		
                //** Response Header 설정
                //=> 방법1: Cookie 객체 사용 (권장)
                //	 Servlet API가 내부적으로 Set-Cookie 헤더를 자동 생성해주는 방식으로
                //	 브라우저가 받게되는 최종 응답은
                //	 Set-Cookie: refreshToken=xxxxx; Path=/; Max-Age=604800; HttpOnly
        		ResponseCookie cookie = ResponseCookie.from("refreshToken", usersDTO.getRefreshToken())
        				.httpOnly(true)  //JS접근불가: document.cookie 로 읽을수없음 (XSS 공격 방어)
                        .sameSite("Lax") //또는 None - sameSite : 보안설정, CORS환경_None
                        .secure(false)	 //HTTP연결 허용
                        .path("/")		//모든 URL요청에 쿠키 포함
                        .maxAge(Duration.ofDays(7)) //쿠키유지시간, 단위 초 (7일 설정)
                        .build();
        		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        		
        		//=> 프론트로 전송하기전 UsersDTO 에서 RefreshToken 정보는 삭제함
                usersDTO.setRefreshToken(null);
                usersDTO.setRefreshTokenExpiresln(null);
        		return usersDTO;
        	}else {
        		throw new Exception("Data Not Found");
        	}
        	
        }catch (Exception e){
			log.error("로그인 실패, Exception => "+e.toString());
    		return null;
        }
    }//login
    
    
    //=> RefreshToken 으로 토큰 재발급
    @Override
    public ResponseEntity<?> getRefresh(String refreshToken, HttpServletResponse response){
    	//=> RefreshToken 검증 : DB 확인
        Optional<RefreshToken> rTokenEntity = refRepository.findByRefreshToken(refreshToken);
    	//=> DB에 RefreshToken이 없으면 유효하지 않음
        if (rTokenEntity.isEmpty()) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        						 .body("isEmpty: 유효하지 않는 RefreshToken");
        }
        log.info("DB조회 성공!!,  존재하는 RefreshToken 입니다.");
        try {
            //** RefreshToken 유효시 AccessToken만 재발급
            //=> RefreshToken 분석 & User id 와 roleList 가져오기
        	Claims claims = tokenProvider.validateToken(refreshToken);
        	//=> 분석과정에서 만료시 ExpiredJwtException 발생 -> catch 로 분기 
        	
        	String userNum = (String)claims.get("userNum");
        	String loginId = (String)claims.get("loginId"); 
            String roleCd = (String)claims.get("roleCd");

            //=> 새로운 accessToken 생성을 위한 claimList 생성
            Map<String, Object> claimList = new HashMap<>(); 
            claimList.put("userNum", userNum);
            claimList.put("loginId", loginId);
            claimList.put("roleCd", roleCd);
            
    		//=> AccessToken 을 담은 UserDTO 객체 생성 & return 
            //	 response 성공시 프론트에서는 accessToken 값만 교체함	
            UsersDTO usersDTO = UsersDTO.builder()
    					.accessToken(tokenProvider.generateAccessToken(claimList))
    					.build();
            
            log.info("New AccessToken 발급, Token="+ usersDTO.getAccessToken());
            //return usersDTO; 
            //-> ResponseEntity 사용안하려니 catch 블럭에서 오류 발생 (코드수정필요해서 일단그냥사용) 
            return ResponseEntity.ok(usersDTO);
            
        }catch(Exception e) {
        	
            log.info("New AccessToken 발급중 RefreshToken 만료 Exception => "+e.toString());
            //=> 쿠키 삭제
            Cookie refreshCookie = new Cookie("refreshToken", null);
            refreshCookie.setPath("/");             // 쿠키 경로 설정 (생성할 때와 같아야 함)
            refreshCookie.setMaxAge(0);             // 유효기간 0 → 삭제
            refreshCookie.setHttpOnly(false);       // 보안 옵션
            refreshCookie.setSecure(true);          // HTTPS만 전달 (필요시)
            response.addCookie(refreshCookie);

            //=> DB에서 삭제
            refRepository.deleteByRefreshToken(refreshToken);
            //return null; 
            //-> ResponseEntity 사용안하려니 catch 블럭에서 오류 발생 (코드수정필요해서 일단그냥사용)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("401, 만료되었습니다. 다시 로그인 해주세요.");
        }//catch
    }
    
    // 로그아웃
    @Override
    public void logout(HttpServletResponse response, Long userNum) {
   	 //=> RefreshToken 제거
        refRepository.deleteByUserNum(userNum);
        
        //=> 쿠키의 refreshToken 삭제
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", null)
                .httpOnly(true) //JS접근불가: document.cookie 로 읽을수없음 (XSS 공격 방어)
                .sameSite("Lax")//또는 None
                .secure(false)	//HTTP연결 허용
                .path("/")		//모든 URL요청에 쿠키 포함
                .maxAge(0) 		//쿠키유지시간 0 설정=>삭제
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
    
    
    // 회원정보 조회
    @Override
    public UsersDTO getUser(String loginId) {
        log.info("** getUser => " + loginId);

        Users users = usersRepository.findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("회원정보가 없습니다"));

        return UsersDTO.builder()
            .userNum(users.getUserNum())
            .loginId(users.getLoginId())
            .userNm(users.getUserNm())
            .tel(users.getTel())
            .email(users.getEmail())
            .genderCd(users.getGenderCd())
            .birthYmd(users.getBirthYmd())
            .statusCd(users.getStatusCd())
            .roleCd(users.getRoleCd())
            .crtAt(users.getCrtAt())
            .build();
    }

    // 회원정보 수정
    @Override
    public void updateUser(String loginId, UsersDTO usersDTO) {
        log.info("** updateUser => " + loginId);

        Users users = usersRepository.findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("회원정보가 없습니다"));

        // 비밀번호 변경시 암호화
        if (usersDTO.getPwd() != null && !usersDTO.getPwd().isEmpty()) {
            users.setPwd(passwordEncoder.encode(usersDTO.getPwd()));
        }

        users.setUserNm(usersDTO.getUserNm());
        users.setTel(usersDTO.getTel());
        users.setEmail(usersDTO.getEmail());
        users.setGenderCd(usersDTO.getGenderCd());
        users.setBirthYmd(usersDTO.getBirthYmd());

        usersRepository.save(users);
    }

    // 회원탈퇴
    @Override
    public void withdraw(String loginId) {
        log.info("** withdraw => " + loginId);

        Users users = usersRepository.findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("회원정보가 없습니다"));

        // 실제 삭제 X, 상태코드만 변경
        users.setStatusCd("W");  // W = 탈퇴
        usersRepository.save(users);
    }
	
}
