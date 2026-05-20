package com.vita.vitapickBack.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vita.vitapickBack.jwtToken.TokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService{

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 아이디 중복확인
    @Override
    public boolean checkLoginId(String loginId) {
        log.info("** checkLoginId => " + loginId);
        return usersRepository.existsByLoginId(loginId);
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

    // 로그인
    @Override
    public Map<String, Object> login(UsersDTO usersDTO) {
        log.info("** login => " + usersDTO.getLoginId());

        // 아이디 확인
        Users users = usersRepository.findByLoginId(usersDTO.getLoginId())
            .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호를 확인해주세요."));

        // 탈퇴회원 확인
        if ("W".equals(users.getStatusCd())) {
            throw new RuntimeException("탈퇴한 회원입니다");
        }
        
        // 비활성회원 확인
        if ("I".equals(users.getStatusCd())) {
            throw new RuntimeException("비활성 회원입니다");
        }
        
        // 비밀번호 확인
        if (!passwordEncoder.matches(usersDTO.getPwd(), users.getPwd())) {
            throw new RuntimeException("아이디 또는 비밀번호를 확인해주세요.");
        }

        // 토큰 생성
        Map<String, Object> claimList = new HashMap<>();
        claimList.put("id", users.getLoginId());
        claimList.put("roleList", List.of(users.getRoleCd()));

        String token = tokenProvider.createToken(claimList, 60); // 60분

        // 응답 데이터
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userNum", users.getUserNum());
        result.put("loginId", users.getLoginId());
        result.put("userNm", users.getUserNm());
        result.put("roleCd", users.getRoleCd());
        //token → 저장해두고 매 요청마다 헤더에 담아서 보냄
        //loginId, userNm → 화면에 표시
        //roleCd → 관리자/일반회원 구분

        return result;
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
