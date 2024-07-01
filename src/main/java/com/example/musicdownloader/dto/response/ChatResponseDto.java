package com.example.musicdownloader.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatResponseDto {
    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    private String type;
}