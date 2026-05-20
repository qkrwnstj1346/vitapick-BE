package com.vita.vitapickBack.cus;
 
import java.time.LocalDateTime;
 
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
 
import com.vita.vitapickBack.users.Users;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@Entity
@Table(name = "cus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cus {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cus_id")
    private Long cusId;
 
    // ── FK: users 테이블 ──────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num", nullable = false)
    private Users users;
 
    // ── AI 모델명 ────────────────────────────────────────────
    @Column(name = "ai_model", length = 100)
    private String aiModel;
 
    // ── 설문 응답 JSON ───────────────────────────────────────
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ans_json", columnDefinition = "JSON")
    private String ansJson;
 
    // ── AI 응답 결과 ─────────────────────────────────────────
    @Column(name = "cus_sum", columnDefinition = "TEXT")
    private String cusSum;        // 커스텀 요약
 
    @Column(name = "cus_reason", columnDefinition = "TEXT")
    private String cusReason;     // 추천 이유
 
    @Column(name = "cus_dos", columnDefinition = "TEXT")
    private String cusDos;        // 복용 가이드
 
    @Column(name = "cus_caution", columnDefinition = "TEXT")
    private String cusCaution;    // 주의 사항
 
    // ── 생성일시 (INSERT 시 자동 세팅) ───────────────────────
    @Column(name = "crt_at", updatable = false)
    private LocalDateTime crtAt;
 
    @PrePersist
    protected void onCreate() {
        this.crtAt = LocalDateTime.now();
    }
}