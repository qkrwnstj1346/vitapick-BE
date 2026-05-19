package com.vita.vitapickBack.chatbot.chat_msg;

import java.util.List;

public interface ChatMsgService {

    List<ChatMsg> findByChatId(Long chatId);
}