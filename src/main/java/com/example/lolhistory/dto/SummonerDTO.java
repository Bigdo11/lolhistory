package com.example.lolhistory.dto;


import lombok.Data;

@Data
public class SummonerDTO {
    private String nickname;
    private String id;
    private String puuid;
    private int summonerLevel;
    private int wins;
    private int losses;
    private int profileIconId;


    // 생성자, 게터 및 세터 생략
}