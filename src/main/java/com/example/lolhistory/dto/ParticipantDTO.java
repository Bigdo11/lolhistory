package com.example.lolhistory.dto;

import lombok.Data;

@Data
public class ParticipantDTO {
    private int assists;
    private int deaths;
    private int kills;
    private boolean win;
    private String championName;
    private int championId;
    private String summonerName;

}
