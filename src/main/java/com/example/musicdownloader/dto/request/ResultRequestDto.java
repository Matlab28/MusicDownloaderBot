package com.example.musicdownloader.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultRequestDto {
    @JsonProperty("update_id")
    private Integer updateId;
    private MessageRequestDto message;
}
