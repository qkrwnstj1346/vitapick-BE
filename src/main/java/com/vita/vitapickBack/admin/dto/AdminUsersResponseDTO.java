package com.vita.vitapickBack.admin.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Excel 다운로드
import java.io.IOException;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUsersResponseDTO {

	private List<AdminUserDTO> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AdminUserDTO {
		private Long userNum;
		private String loginId;
		private String userNm;
		private String tel;
		private String statusCd;
		private String roleCd;
		private LocalDateTime crtAt;
		private LocalDateTime updAt;
		private LocalDateTime wdDt;
	}
}
