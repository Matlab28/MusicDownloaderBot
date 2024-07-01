package com.example.musicdownloader.controller;

import com.example.musicdownloader.dto.request.RootRequestDto;
import com.example.musicdownloader.dto.request.TelegramSendDto;
import com.example.musicdownloader.dto.response.RootResponseDto;
import com.example.musicdownloader.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram")
public class BotController {
    private final MusicService service;

    @GetMapping("/get-updates")
    public RootRequestDto getUpdates() {
        return service.getUpdateService();
    }

    @PostMapping("/send-message")
    public RootResponseDto sendMessage(@RequestBody TelegramSendDto dto) {
        return service.sendMessage(dto);
    }
}
