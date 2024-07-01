package com.example.musicdownloader.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Root {
    private String link;
    private String title;
    private Integer filesize;
    private Integer progress;
    private Double duration;
    private String status;
    private String msg;
}
