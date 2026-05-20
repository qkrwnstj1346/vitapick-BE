package com.vita.vitapickBack.cus;
 
import com.vita.vitapickBack.products.prd.Prd;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@Entity
@Table(name = "cus_it")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CusIt {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cus_it_id")
    private Long cusItId;
 
    // ── FK: cus 테이블 ───────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cus_id", nullable = false)
    private Cus cus;
 
    // ── FK: prd 테이블 ───────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prd_id", nullable = false)
    private Prd prd;
 
    // ── 추천 순서 (1~5) ──────────────────────────────────────
    @Column(name = "sort_num", nullable = false)
    private Integer sortNum;
}