package com.vita.vitapickBack.users;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	
	//로그인 아이디로 회원 찾기 (로그인, 중복확인)
	Optional<Users> findByLoginId(String loginId);
	
    // 아이디 중복확인
    boolean existsByLoginId(String loginId);

    // 이메일 중복확인
    boolean existsByEmail(String email);
    
    // 아이디찾기(이름, 이메일)
    Optional<Users> findByUserNmAndEmail(String userNm, String email);

    // 비밀번호찾기(아이디, 이름, 이메일)
    Optional<Users> findByLoginIdAndUserNmAndEmail(String loginId, String userNm, String email);

    Long countByStatusCd(String statusCd);

    @Query("""
            SELECT u
            FROM Users u
            WHERE (:keyword IS NULL OR :keyword = ''
                   OR LOWER(u.loginId) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(u.userNm) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:statusCd IS NULL OR :statusCd = '' OR u.statusCd = :statusCd)
              AND (:roleCd IS NULL OR :roleCd = '' OR u.roleCd = :roleCd)
            """)
    Page<Users> findAdminUsers(
            @Param("keyword") String keyword,
            @Param("statusCd") String statusCd,
            @Param("roleCd") String roleCd,
            Pageable pageable);
     
}
