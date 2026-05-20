package com.vita.vitapickBack.products.prd_img;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdImgRepository extends JpaRepository<PrdImg, Long> {
    List<PrdImg> findByPrdId(Long prdId);
}
