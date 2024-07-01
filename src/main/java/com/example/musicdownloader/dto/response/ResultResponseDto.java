package com.example.musicdownloader.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultResponseDto {
    @JsonProperty("message_id")
    private Integer messageId;
    private FromResponseDto from;
    private ChatResponseDto chat;
    private Integer date;
    private String text;
}
