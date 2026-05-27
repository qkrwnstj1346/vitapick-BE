package com.vita.vitapickBack.cart;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartDTO {

	// 장바구니 
	private Long cartId;
	private Long userNum;
	private Long prdId;
	private Long cusId;
	private Integer itQty;
	private Character selectedYn;
	private LocalDateTime crtAt;
	private LocalDateTime updAt;
	
	// 장바구니 상품 정보 
    private String prdNm;
    private Integer price;
    private String brand;
    private String thumbImgUrl;

}