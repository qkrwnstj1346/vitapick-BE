package com.vita.vitapickBack.chatbot.chat_room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import com.vita.vitapickBack.chatbot.chat_msg.ChatMsg;
import com.vita.vitapickBack.chatbot.chat_msg.ChatMsgRepository;
import com.vita.vitapickBack.chatbot.chat_prd.ChatPrdDto;
import com.vita.vitapickBack.chatbot.chat_prd.ChatPrdService;
import com.vita.vitapickBack.products.prd.Prd;
import com.vita.vitapickBack.products.prd.PrdRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMsgRepository chatMsgRepository;
    private final OpenAiChatModel openAiChatModel;
    private final ChatPrdService chatPrdService;
    private final PrdRepository prdRepository;

    @Override
    public ChatMsg chatMsg(ChatRoomDto dto) {

        // 1. 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .userNum(dto.getUserNum())
                .chatStCd("ACTIVE")
                .crtAt(LocalDateTime.now())
                .updAt(LocalDateTime.now())
                .build();
        chatRoom = chatRoomRepository.save(chatRoom);

        // 2. 사용자 메시지 저장
        ChatMsg userMsg = ChatMsg.builder()
                .chatId(chatRoom.getChatId())
                .senderCd("USER")
                .msgTxt(dto.getMsgTxt())
                .crtAt(LocalDateTime.now())
                .build();
        chatMsgRepository.save(userMsg);

        // 3. DB에서 상품 목록 가져오기
        List<Prd> prdList = prdRepository.findAll();
        String prdInfo = prdList.stream()
                .map(p -> "상품ID:" + p.getPrdId() 
                        + " 상품명:" + p.getPrdNm() 
                        + " 성분:" + p.getIngr()
                        + " 주의사항:" + p.getWarnTxt())
                .collect(Collectors.joining("\n"));

     // 4. GPT 호출
        String prompt = "당신은 비타민 및 건강기능식품 전문가입니다.\n"
                + "아래는 우리 쇼핑몰 상품 목록입니다:\n"
                + prdInfo + "\n\n"
                + "사용자 요청: " + dto.getMsgTxt() + "\n\n"
                + "위 상품 목록 중에서만 선택하고 아래 형식으로만 대답하세요:\n"
                + "1. 한줄요약: (짧게 한 줄)\n"
                + "2. 추천상품:\n"
                + "- 상품ID: X / 상품명: XXX\n"
                + "- 상품ID: X / 상품명: XXX\n"
                + "- 상품ID: X / 상품명: XXX\n"
                + "3. 조합이유: (위 상품들을 함께 섭취했을 때 시너지 효과와 이유를 설명)\n"
                + "4. 주의사항: (성분 과다섭취, 알레르기 등 간단히)\n"
                + "반드시 이 형식만 사용하고 다른 말은 하지 마세요.";
        String gptResponse = openAiChatModel.call(prompt);

        // 5. 봇 응답 저장
        ChatMsg botMsg = ChatMsg.builder()
                .chatId(chatRoom.getChatId())
                .senderCd("AI")
                .msgTxt(gptResponse)
                .aiModel("gpt-4o-mini")
                .crtAt(LocalDateTime.now())
                .build();
        botMsg = chatMsgRepository.save(botMsg);

        // 6. 추천상품 저장
        ChatPrdDto chatPrdDto = new ChatPrdDto(botMsg.getMsgId(), null, 1, gptResponse);
        chatPrdService.saveChatPrd(chatPrdDto);

        return botMsg;
    }
}
