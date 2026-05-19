package com.vita.vitapickBack.chatbot.chat_prd;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatPrdServiceImpl implements ChatPrdService {
	
	private final ChatPrdRepository chatPrdRepository;
	
	@Override
	public ChatPrd saveChatPrd(ChatPrdDto dto) {
		 // 추천상품 저장
		ChatPrd chatPrd = ChatPrd.builder()
				.msgId(dto.getMsgId())
				.prdId(dto.getPrdId())
				.sortNum(dto.getSortNum())
				.chatRecReason(dto.getChatRecReason())
				.build();
		return chatPrdRepository.save(chatPrd);
	}
	
}
