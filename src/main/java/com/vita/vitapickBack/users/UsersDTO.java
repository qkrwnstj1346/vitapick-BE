package com.vita.vitapickBack.users;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDTO {
    private Long userNum;
    private String loginId;
    private String pwd;
    private String userNm;
    private String tel;
    private String email;
    private String genderCd;
    private LocalDate birthYmd;
    private String statusCd;
    private String roleCd;
    private LocalDateTime crtAt;
    private LocalDateTime updAt;
    private LocalDateTime wdDt;
}
