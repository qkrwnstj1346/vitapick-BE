package com.vita.vitapickBack.products.prd_img;

import java.util.List;

public interface PrdImgService {

    List<PrdImg> findByPrdId(Long prdId);
}