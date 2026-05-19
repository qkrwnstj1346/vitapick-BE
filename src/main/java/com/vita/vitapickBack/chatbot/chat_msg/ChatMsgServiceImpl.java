package com.vita.vitapickBack.chatbot.chat_msg;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMsgServiceImpl implements ChatMsgService {

    private final ChatMsgRepository chatMsgRepository;

    @Override
    public List<ChatMsg> findByChatId(Long chatId) {
        return chatMsgRepository.findByChatId(chatId);
    }
}