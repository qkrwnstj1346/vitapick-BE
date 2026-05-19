package com.vita.vitapickBack.chatbot.chat_prd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatPrdDto {
	private Long msgId;
	private Long prdId;
	private Integer sortNum;
	private String chatRecReason;
}
