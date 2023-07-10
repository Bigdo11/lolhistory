package com.example.lolhistory.dto;


import lombok.Data;

@Data
public class SummonerDTO {
    private String nickname;
    private String id;
    private String puuid;
    private String name;
    private int summonerLevel;
    private int profileIconId;
    private int wins;
    private int losses;
    // 생성자, 게터 및 세터 생략
}