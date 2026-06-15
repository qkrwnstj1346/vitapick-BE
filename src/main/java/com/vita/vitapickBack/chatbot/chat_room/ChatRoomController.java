package com.vita.vitapickBack.chatbot.chat_room;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vita.vitapickBack.chatbot.chat_msg.ChatMsg;

import lombok.RequiredArgsConstructor;

// 챗봇 메시지 요청 창구
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatbot")
public class ChatRoomController {
	private final ChatRoomService chatRoomService;
	
	@PostMapping("/message")
	public ResponseEntity<ChatMsg> chatMsg(
	        @AuthenticationPrincipal Long userNum,
	        @RequestBody ChatRoomDto dto) {

	    ChatMsg result = chatRoomService.chatMsg(userNum, dto);
	    return ResponseEntity.ok(result);
	}
	
	// 마이페이지 - 내 챗봇 상담방 목록 조회
	@GetMapping("/rooms")
	public ResponseEntity<List<ChatRoomMPDto>> getMyChatRooms(
	        @AuthenticationPrincipal Long userNum) {

	    List<ChatRoomMPDto> result = chatRoomService.getMyChatRooms(userNum);

	    return ResponseEntity.ok(result);
	}
}
