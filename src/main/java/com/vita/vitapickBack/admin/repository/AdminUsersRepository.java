package com.vita.vitapickBack.admin.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vita.vitapickBack.users.Users;

@Repository
public interface AdminUsersRepository extends JpaRepository<Users, Long> {

    @Query("""
            SELECT u
            FROM Users u
            WHERE (:keyword IS NULL OR :keyword = ''
                   OR LOWER(u.loginId) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(u.userNm) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:statusCd IS NULL OR :statusCd = '' OR u.statusCd = :statusCd)
              AND u.roleCd = 'USER'
              AND (:startAt IS NULL OR u.crtAt >= :startAt)
              AND (:endAt IS NULL OR u.crtAt < :endAt)
            """)
    Page<Users> findAdminUsers(
            @Param("keyword") String keyword,
            @Param("statusCd") String statusCd,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            Pageable pageable);
}
