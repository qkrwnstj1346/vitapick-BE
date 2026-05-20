package com.vita.vitapickBack.chatbot.chat_room;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	
	Optional<ChatRoom> findTopByUserNumAndChatStCd(Long userNum, String chatStCd);
}
