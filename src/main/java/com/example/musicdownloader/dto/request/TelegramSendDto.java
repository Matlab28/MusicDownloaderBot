package com.example.musicdownloader.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TelegramSendDto {
    @JsonProperty("chat_id")
    private String chatId;
    private String text;
}
