package com.example.musicdownloader.client;

import com.example.musicdownloader.dto.Root;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "mp3API", url = "https://youtube-mp36.p.rapidapi.com/dl?id=UxxajLWwzqY")
public interface MusicApiClient {
    @GetMapping
    Root getData(@RequestHeader("x-rapidapi-host") String host,
                 @RequestHeader("x-rapidapi-key") String apiKey);
}
