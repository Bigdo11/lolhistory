package com.example.lolhistory.service;

import com.example.lolhistory.dto.MatchDTO;
import com.example.lolhistory.dto.SummonerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SummonerService {

    private final WebClient webClient;
    private final String apiKey;

    public SummonerService(WebClient.Builder webClientBuilder, @Value("${riotAPI}") String apiKey) {
        this.webClient = webClientBuilder.build();
        this.apiKey = apiKey;
    }

    public String getSummonerIdByNickname(String nickname) {
        String summonerUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{nickname}?api_key="+apiKey;

        SummonerDTO summonerDTO = webClient.get()
                .uri(summonerUrl, nickname)
                .retrieve()
                .bodyToMono(SummonerDTO.class)
                .block();

        if (summonerDTO != null) {
            return summonerDTO.getId();
        }

        return null;
    }
    //닉네임으로 puuid가져오기
    public String getPuuidBySummonerName(String nickname) {
        String summonerUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{summonerName}?api_key=" + apiKey;

        SummonerDTO summonerDTO = webClient.get()
                .uri(summonerUrl, nickname)
                .retrieve()
                .bodyToMono(SummonerDTO.class)
                .block();

        return summonerDTO != null ? summonerDTO.getPuuid() : "";
    }

    public List<String> getMatchIdByPuuid(String puuid) {
        String matchesUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?start=0&count=20&api_key=" + apiKey;

        List<String> matchIds = webClient.get()
                .uri(matchesUrl, puuid)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();

        if (matchIds != null) {
            return matchIds;
        }

        return Collections.emptyList();
    }



    public int getSummonerLevelByNickname(String nickname) {
        String summonerUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{nickname}?api_key=" + apiKey;


        SummonerDTO summonerDTO = webClient.get()
                .uri(summonerUrl, nickname)
                .retrieve()
                .bodyToMono(SummonerDTO.class)
                .block();

        if (summonerDTO != null) {
            return summonerDTO.getSummonerLevel();
        }

        return 0;
    }
    public int getSummonerIconByNickname(String nickname) {
        String summonerUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{nickname}?api_key=" + apiKey;

        SummonerDTO summonerDTO = webClient.get()
                .uri(summonerUrl, nickname)
                .retrieve()
                .bodyToMono(SummonerDTO.class)
                .block();

        if (summonerDTO != null) {
            // 소환사 아이콘 이미지 URL 반환
            return summonerDTO.getProfileIconId();
        }

        // 예외 처리: 아이콘 이미지를 가져오지 못한 경우 null 또는 다른 처리를 수행할 수 있습니다.
        return 0;
    }

    public String getLatestDdragonVersion() {
        String versionUrl = "https://ddragon.leagueoflegends.com/api/versions.json";

        String[] versions = webClient.get()
                .uri(versionUrl)
                .retrieve()
                .bodyToMono(String[].class)
                .block();

        return versions != null ? versions[0] : null;
    }

    public String getSummonerIconUrl(int iconId, String version) {
        return String.format("https://ddragon.leagueoflegends.com/cdn/%s/img/profileicon/%d.png", version, iconId);
    }

    public String getWin
}
