package com.vita.vitapickBack.chatbot.chat_prd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatPrdRepository extends JpaRepository<ChatPrd, Long> {

}
