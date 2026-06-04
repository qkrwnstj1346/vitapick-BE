package com.vita.vitapickBack.users;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;

public interface UsersService {
    
    // 아이디 중복확인
    boolean checkLoginId(String loginId);
    
    // 이메일 중복확인
    boolean checkEmail(String email);
    
    // 아이디 찾기
    public UsersDTO findId(UsersDTO usersDTO);
    
    // 회원가입
    void signup(UsersDTO usersDTO);

    // 로그인
    public UsersDTO login(HttpServletResponse response, Users entity);
    
    //=> RefreshToken 으로 토큰 재발급
    public ResponseEntity<?> getRefresh(String refreshToken, HttpServletResponse response);
    
    // 로그아웃
    public void logout(HttpServletResponse response, Long userNum);

    // 회원정보 조회
    public UsersDTO getUser(String loginId);

    // 회원정보 수정
    void updateUser(String loginId, UsersDTO usersDTO);

    // 회원탈퇴
    void withdraw(String loginId);

}
