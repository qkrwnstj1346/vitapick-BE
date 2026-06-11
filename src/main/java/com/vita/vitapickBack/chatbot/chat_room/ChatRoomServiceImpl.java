package com.vita.vitapickBack.chatbot.chat_room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import com.vita.vitapickBack.chatbot.chat_msg.ChatMsg;
import com.vita.vitapickBack.chatbot.chat_msg.ChatMsgRepository;
import com.vita.vitapickBack.chatbot.chat_prd.ChatPrdDto;
import com.vita.vitapickBack.chatbot.chat_prd.ChatPrdService;
import com.vita.vitapickBack.products.prd.Prd;
import com.vita.vitapickBack.products.prd.PrdRepository;

import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMsgRepository chatMsgRepository;
    private final OpenAiChatModel openAiChatModel;
    private final ChatPrdService chatPrdService;
    private final PrdRepository prdRepository;
    private final ObjectMapper objectMapper;
    
    @Override
    public ChatMsg chatMsg(Long userNum, ChatRoomDto dto) {

        // 0. 값 검증
        if (userNum == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        if (dto == null || dto.getMsgTxt() == null || dto.getMsgTxt().trim().isEmpty()) {
            throw new RuntimeException("메시지 내용이 없습니다.");
        }

        // 1. 기존 ACTIVE 채팅방 있으면 재사용, 없으면 새로 생성
        Optional<ChatRoom> found = chatRoomRepository.findTopByUserNumAndChatStCd(userNum, "ACTIVE");

        ChatRoom chatRoom;
        if (found.isPresent()) {
            chatRoom = found.get();
        } else {
            chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                    .userNum(userNum)
                    .chatStCd("ACTIVE")
                    .crtAt(LocalDateTime.now())
                    .updAt(LocalDateTime.now())
                    .build()
            );
        }

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
        String prdInfo = "";
        for (Prd p : prdList) {
            prdInfo += "상품ID:" + p.getPrdId()
                    + " 상품명:" + p.getPrdNm()
                    + " 성분:" + p.getIngr()
                    + " 주의사항:" + p.getWarnTxt() + "\n";
        }

     // 4. GPT 호출
        String prompt = "당신은 비타민 및 건강기능식품 전문가입니다.\n"
                + "아래는 우리 쇼핑몰 상품 목록입니다:\n"
                + prdInfo + "\n\n"
                + "사용자 요청: " + dto.getMsgTxt() + "\n\n"
                + "위 상품 목록 중에서만 선택하고 아래 규칙을 반드시 지키세요:\n"
                + "규칙1. 추천 상품은 반드시 서로 다른 성분의 상품으로 3가지를 고르세요.\n"
                + "규칙2. 같은 성분(예: 비타민D)이 포함된 상품을 2개 이상 추천하지 마세요. 성분이 겹치면 과다복용이 됩니다.\n"
                + "규칙3. 사용자의 증상이나 요청에 맞는 다양한 영양소를 조합해서 추천하세요.\n"
                + "규칙4. 추천 상품들의 주의사항 컬럼을 확인해서 성분 충돌이나 과다복용 위험이 있으면 제외하세요.\n"
                + "규칙5. 반드시 아래 JSON 형식으로만 대답하세요. 다른 말은 절대 하지 마세요.\n\n"
                + "{\n"
                + "  \"reason\": \"사용자 증상에 맞는 추천 이유 한 줄\",\n"
                + "  \"products\": [\n"
                + "    {\"prd_id\": 숫자, \"sort_num\": 1},\n"
                + "    {\"prd_id\": 숫자, \"sort_num\": 2},\n"
                + "    {\"prd_id\": 숫자, \"sort_num\": 3}\n"
                + "  ],\n"
                + "  \"comboReason\": \"조합이유 2-3줄\",\n"
                + "  \"caution\": \"주의사항 한 줄\"\n"
                + "}\n";
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

        // 6. GPT 응답 JSON 파싱
        JsonNode root;

        try {
            root = objectMapper.readTree(gptResponse);
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 JSON 파싱 실패");
        }

        // 7. products 배열 꺼내기
        JsonNode products = root.path("products");

        // 8. 추천상품 저장
        if (products.isArray()) {
            for (JsonNode item : products) {

                Long prdId = item.path("prd_id").asLong();
                Integer sortNum = item.path("sort_num").asInt();

                ChatPrdDto chatPrdDto = ChatPrdDto.builder()
                        .msgId(botMsg.getMsgId())
                        .prdId(prdId)
                        .sortNum(sortNum)
                        .chatRecReason(gptResponse)
                        .build();
  
                chatPrdService.saveChatPrd(chatPrdDto);
            }
        }

        return botMsg;
    }
}
