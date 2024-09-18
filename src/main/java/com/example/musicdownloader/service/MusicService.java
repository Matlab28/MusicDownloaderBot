package com.example.musicdownloader.service;

import com.example.musicdownloader.client.MusicApiClient;
import com.example.musicdownloader.client.TelegramAPIClient;
import com.example.musicdownloader.dto.Root;
import com.example.musicdownloader.dto.request.RootRequestDto;
import com.example.musicdownloader.dto.request.TelegramSendDto;
import com.example.musicdownloader.dto.response.RootResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class MusicService {
    private final TelegramAPIClient telegramClient;
    private final MusicApiClient client;
    private final String host = "YOUR_HOST";
    private final String key = "YOUR_API_KEY";
    private Long lastUpdateId = 0L;

    public MusicService(TelegramAPIClient telegramClient,
                        MusicApiClient client) {
        this.telegramClient = telegramClient;
        this.client = client;
    }

    public RootRequestDto getUpdateService() {
        RootRequestDto updates = telegramClient.getUpdates(0L);
        if (!updates.getResult().isEmpty()) {
            Integer updateId = updates.getResult().get(updates.getResult().size() - 1).getUpdateId();
            log.info("Message got from - " + updates.getResult().get(0).getMessage().getFrom().getFirstName() + ", ID - "
                    + updates.getResult().get(0).getMessage().getChat().getId());
            return telegramClient.getUpdates(Long.valueOf(updateId));
        }
        return null;
    }

    public Root getUpdates() {
        return client.getData(host, key, "nREKA3M4XSA");
    }

    public RootResponseDto sendMessage(TelegramSendDto dto) {
        return telegramClient.sendMessage(dto);
    }

    private String extractVideoId(String url) {
        String pattern = "(?:https?:\\/\\/)?(?:www\\.)?(?:youtube\\.com\\/.*v=|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public void handleTelegramUpdate(String text, Long chatId) {
        RootRequestDto updateService = getUpdateService();
        TelegramSendDto dto = new TelegramSendDto();
        dto.setChatId(String.valueOf(chatId));

        if (text.equals("/start")) {
            text = "Hi " + updateService.getResult().get(0).getMessage().getFrom().getFirstName()
                    + "!\nPlease provide me a YouTube link URL for converting MP3 file 🎶";
            log.info(updateService.getResult().get(0).getMessage().getFrom().getFirstName() + ", ID"
                    + updateService.getResult().get(0).getMessage().getChat().getId() + " joined to bot.");
            dto.setText(text);
        } else {
            String videoId = extractVideoId(text);
            if (videoId == null) {
                dto.setText(updateService.getResult().get(0).getMessage().getFrom().getFirstName() +
                        ", your URL is invalid. Please provide me only YouTube links.");
                log.error("Invalid URL from - " + updateService.getResult().get(0).getMessage().getFrom().getFirstName()
                        + ", ID - " + updateService.getResult().get(0).getMessage().getChat().getId());
            } else {
                Root root = client.getData(host, key, videoId);

                if (root != null && root.getLink() != null) {
                    dto.setText("Here is your MP3 download link: " + root.getLink());
                    log.info("MP3 link sent to - "
                            + updateService.getResult().get(0).getMessage().getFrom().getFirstName()
                            + ", ID - " + updateService.getResult().get(0).getMessage().getChat().getId());
                } else {
                    dto.setText("Sorry, I couldn't convert the video to MP3.");
                }
            }
        }

        sendMessage(dto);
    }

    public RootResponseDto sendInfo() {
        RootRequestDto updateService = getUpdateService();
        if (updateService != null) {
            String text = updateService.getResult().get(0).getMessage().getText();
            Long id = updateService.getResult().get(0).getMessage().getChat().getId();
            handleTelegramUpdate(text, id);


        }
        return null;
    }

    @Scheduled(fixedDelay = 1000)
    public void refresh() {
        RootRequestDto updateService = getUpdateService();
        if (updateService != null && !updateService.getResult().isEmpty()) {
            Integer latestUpdateId = updateService.getResult().get(updateService.getResult().size() - 1).getUpdateId();
            if (latestUpdateId > lastUpdateId) {
                lastUpdateId = Long.valueOf(latestUpdateId);
                sendInfo();
            }
        }
    }
}


//    public void handleTelegramUpdate(String text, Long chatId) {
//        RootRequestDto updateService = getUpdateService();
//        TelegramSendDto dto = new TelegramSendDto();
//        dto.setChatId(String.valueOf(chatId));
//
//        String videoId = extractVideoId(text);
//        if (videoId == null) {
//            dto.setText(updateService.getResult().get(0).getMessage().getFrom().getFirstName() +
//                    ", your URL is invalid. Please provide me only YouTube links.");
//            log.error("Invalid URL from - " + updateService.getResult().get(0).getMessage().getFrom().getFirstName()
//                    + ", ID - " + updateService.getResult().get(0).getMessage().getChat().getId());
//        } else {
//            Root root = client.getData(host, key, videoId);
//
//            if (root != null && root.getLink() != null) {
//                dto.setText("Here is your MP3 download link: " + root.getLink());
//                log.info("MP3 link sent to - "
//                        + updateService.getResult().get(0).getMessage().getFrom().getFirstName()
//                        + ", ID - " + updateService.getResult().get(0).getMessage().getChat().getId());
//            } else {
//                dto.setText("Sorry, I couldn't convert the video to MP3.");
//            }
//        }
//
//        sendMessage(dto);
//    }
