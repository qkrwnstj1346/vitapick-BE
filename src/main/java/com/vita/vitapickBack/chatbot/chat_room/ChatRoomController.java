package com.vita.vitapickBack.chatbot.chat_room;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vita.vitapickBack.chatbot.chat_msg.ChatMsg;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatbot")
public class ChatRoomController {
	private final ChatRoomService chatRoomService;
	
	@PostMapping("/message")
    public ResponseEntity<ChatMsg> chatMsg(@RequestBody ChatRoomDto dto) {
        ChatMsg result = chatRoomService.chatMsg(dto);
        return ResponseEntity.ok(result);
    }
}
