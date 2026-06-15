package com.vita.vitapickBack.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vita.vitapickBack.admin.AdminUsersResponseDTO.AdminUserDTO;
import com.vita.vitapickBack.users.Users;
import com.vita.vitapickBack.users.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUsersService {

    private final UsersRepository usersRepository;

    public AdminUsersResponseDTO getUsers(int page, int size, String keyword, String statusCd, String roleCd) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : Math.min(size, 100);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "userNum"));

        Page<Users> usersPage = usersRepository.findAdminUsers(keyword, statusCd, roleCd, pageable);

        return AdminUsersResponseDTO.builder()
                .content(usersPage.getContent().stream()
                        .map(this::toAdminUserDTO)
                        .toList())
                .page(usersPage.getNumber())
                .size(usersPage.getSize())
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .build();
    }

    private AdminUserDTO toAdminUserDTO(Users users) {
        return AdminUserDTO.builder()
                .userNum(users.getUserNum())
                .loginId(users.getLoginId())
                .userNm(users.getUserNm())
                .tel(users.getTel())
                .statusCd(users.getStatusCd())
                .roleCd(users.getRoleCd())
                .crtAt(users.getCrtAt())
                .updAt(users.getUpdAt())
                .wdDt(users.getWdDt())
                .build();
    }
}
