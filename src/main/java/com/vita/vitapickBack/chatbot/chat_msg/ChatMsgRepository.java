package com.vita.vitapickBack.chatbot.chat_msg;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {
    List<ChatMsg> findByChatId(Long chatId);
}