package com.vita.vitapickBack.users;

import java.util.Map;

public interface UsersService {
    
    // 아이디 중복확인
    boolean checkLoginId(String loginId);

    // 회원가입
    void signup(UsersDTO usersDTO);

    // 로그인
    Map<String, Object> login(UsersDTO usersDTO);

    // 회원정보 조회
    UsersDTO getUser(String loginId);

    // 회원정보 수정
    void updateUser(String loginId, UsersDTO usersDTO);

    // 회원탈퇴
    void withdraw(String loginId);

}
