package com.example.musicdownloader.client;

import com.example.musicdownloader.dto.request.RootRequestDto;
import com.example.musicdownloader.dto.request.TelegramSendDto;
import com.example.musicdownloader.dto.response.RootResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "telegramApi", url = "https://api.telegram.org/bot6707372072:AAFLp0wSjvc_WUMX2MQ1_HLwNAKA-Z3ZQeI")
public interface TelegramAPIClient {
    @GetMapping("/getUpdates?offset={value}")
    RootRequestDto getUpdates(@PathVariable Long value);

    @PostMapping("/sendMessage")
    RootResponseDto sendMessage(@RequestBody TelegramSendDto dto);
}
