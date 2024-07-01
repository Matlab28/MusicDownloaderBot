package com.example.musicdownloader.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageRequestDto {
    @JsonProperty("message_id")
    private Integer messageId;
    private FromRequestDto from;
    private ChatRequestDto chat;
    private Integer date;
    private String text;
}
