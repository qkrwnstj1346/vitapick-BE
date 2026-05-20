package com.vita.vitapickBack.custom.sur;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurServiceImpl implements SurService{

	private final SurRepository surRepository;
	
	@Override
		public SurDTO saveSurvey(SurDTO dto) {
		
			//타이틀에 값이 들어오면 그 값을 타이틀로 저장하고 아니면 지금 날짜시간을 타이틀로 함
			String title =(dto.getSurTitle() != null && !dto.getSurTitle().isBlank())
					? dto.getSurTitle()
					: LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm"));
			
			Sur sur = Sur.builder()
					.surTitle(title)
					.userNum(dto.getUserNum())
					.ansJson(dto.getAnsJson())
					.build();
			
			Sur saved = surRepository.save(sur);
			
			return SurDTO.builder()
					.surId(saved.getSurId())
					.message("설문이 저장되었습니다.")
					.build();
					
		}
}
