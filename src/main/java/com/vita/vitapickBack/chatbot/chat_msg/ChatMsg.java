package com.vita.vitapickBack.chatbot.chat_msg;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="chat_msg")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msg_id")
    private Long msgId;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "sender_cd", nullable = false, length = 30)
    private String senderCd;

    @Column(name = "msg_txt", columnDefinition = "TEXT", nullable = false)
    private String msgTxt;

    @Column(name = "ai_model", length = 100)
    private String aiModel;

    @Column(name = "crt_at", nullable = false)
    private LocalDateTime crtAt;
}