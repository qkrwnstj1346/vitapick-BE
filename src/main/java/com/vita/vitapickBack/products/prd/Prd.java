package com.vita.vitapickBack.products.prd;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="prd")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prdId;
    private String prdNm;
    private Integer catCd;
    private String brand;
    private Integer price;
    private String descTxt;
    private String dosTxt;
    private String warnTxt;
    private String useYn;
    private LocalDateTime crtAt;
    private LocalDateTime updAt;
    private LocalDateTime wdAt;
    //test
}