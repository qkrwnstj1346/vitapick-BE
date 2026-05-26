package com.vita.vitapickBack.chatbot.chat_msg;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 메시지 DB 조회 창구
@Repository
public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {
	
    // 채팅방 ID로 전체 메시지 목록 조회
    List<ChatMsg> findByChatId(Long chatId);
}