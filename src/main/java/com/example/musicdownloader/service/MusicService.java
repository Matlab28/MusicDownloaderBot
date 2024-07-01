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
    private final String host = "YOUR_API_HOST";
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

        String videoId = extractVideoId(text);
        if (videoId == null) {
            dto.setText(updateService.getResult().get(0).getMessage().getFrom().getFirstName() +
                    ", your URL is invalid. Please provide me only YouTube links.");
        } else {
            Root root = client.getData(host, key, videoId);

            if (root != null && root.getLink() != null) {
                dto.setText("Here is your MP3 download link: " + root.getLink());
            } else {
                dto.setText("Sorry, I couldn't convert the video to MP3.");
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



//package com.example.musicdownloader.service;
//
//import com.example.musicdownloader.client.MusicApiClient;
//import com.example.musicdownloader.client.TelegramAPIClient;
//import com.example.musicdownloader.dto.Root;
//import com.example.musicdownloader.dto.request.RootRequestDto;
//import com.example.musicdownloader.dto.request.TelegramSendDto;
//import com.example.musicdownloader.dto.response.RootResponseDto;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class MusicService {
//    private final TelegramAPIClient telegramClient;
//    private final MusicApiClient client;
//    private final String host = "youtube-mp36.p.rapidapi.com";
//    private final String key = "674678e122mshd00ec5b8f945302p1052bcjsn0ad69ed2af91";
//    private Long lastUpdateId = 0L;
//
//    public MusicService(TelegramAPIClient telegramClient,
//                        MusicApiClient client) {
//        this.telegramClient = telegramClient;
//        this.client = client;
//    }
//
//    public RootRequestDto getUpdateService() {
//        RootRequestDto updates = telegramClient.getUpdates(0L);
//        if (!updates.getResult().isEmpty()) {
//            Integer updateId = updates.getResult().get(updates.getResult().size() - 1).getUpdateId();
//            log.info("Message got from - " + updates.getResult().get(0).getMessage().getFrom().getFirstName() + ", ID - "
//                    + updates.getResult().get(0).getMessage().getChat().getId());
//            return telegramClient.getUpdates(Long.valueOf(updateId));
//        }
//        return null;
//    }
//
//    public Root getUpdates() {
//        return client.getData(host, key);
//    }
//
//
//    public RootResponseDto sendMessage(TelegramSendDto dto) {
//        return telegramClient.sendMessage(dto);
//    }
//
//    public void handleTelegramUpdate(String text, Long chatId) {
//        RootRequestDto updateService = getUpdateService();
//        TelegramSendDto dto = new TelegramSendDto();
//        dto.setChatId(String.valueOf(chatId));
//
//        if (!text.matches("https://youtu\\.be/[^\\s]+")) {
//            dto.setText(updateService.getResult().get(0).getMessage().getFrom().getFirstName() +
//                    ", your URL is invalid. Please provide me only youtube links.");
//        }
//
//        if (text != null && text.startsWith("http")) {
//            Root root = client.getData(host, key);
//
//            if (root != null && root.getLink() != null) {
//                dto.setText("Here is your MP3 download link: " + root.getLink());
//            } else {
//                dto.setText("Sorry, I couldn't convert the video to MP3.");
//            }
//        } else if ("/start".equals(text)) {
//            String msg = "Hi " + updateService.getResult().get(0).getMessage().getFrom().getFirstName() +
//                    ", welcome to our bot!\n\nTo download MP3 from YouTube, just send the YouTube URL.";
//            dto.setText(msg);
//        }
//
//        sendMessage(dto);
//    }
//
//    public RootResponseDto sendInfo() {
//        RootRequestDto updateService = getUpdateService();
//        if (updateService != null) {
//            String text = updateService.getResult().get(0).getMessage().getText();
//            Long id = updateService.getResult().get(0).getMessage().getChat().getId();
//            handleTelegramUpdate(text, id);
//        }
//        return null;
//    }
//
//    @Scheduled(fixedDelay = 1000)
//    public void refresh() {
//        RootRequestDto updateService = getUpdateService();
//        if (updateService != null && !updateService.getResult().isEmpty()) {
//            Integer latestUpdateId = updateService.getResult().get(updateService.getResult().size() - 1).getUpdateId();
//            if (latestUpdateId > lastUpdateId) {
//                lastUpdateId = Long.valueOf(latestUpdateId);
//                sendInfo();
//            }
//        }
//    }
//}
