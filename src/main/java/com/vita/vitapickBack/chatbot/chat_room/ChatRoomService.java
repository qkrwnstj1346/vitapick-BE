package com.vita.vitapickBack.chatbot.chat_room;

import com.vita.vitapickBack.chatbot.chat_msg.ChatMsg;

// 챗봇 서비스 인터페이스
public interface ChatRoomService {
    // 유저 메시지 받아서 GPT 호출 후 AI 응답 반환
	ChatMsg chatMsg(Long userNum, ChatRoomDto dto);
}
