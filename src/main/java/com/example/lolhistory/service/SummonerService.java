package com.example.lolhistory.service;

import com.example.lolhistory.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    //id가져오기
    public String getSummonerIdByNickname(String nickname) {
        String summonerUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{nickname}?api_key=" + apiKey;

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

    //puuid로 매치id가져오기
    public List<String> getMatchIdByPuuid(String puuid) {
        String matchesUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?start=0&count=20&api_key=" + apiKey;

        List<String> matchIds = webClient.get()
                .uri(matchesUrl, puuid)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();

        if (matchIds != null) {
            return matchIds;
        }

        return Collections.emptyList();
    }

    public List<String> getSummonerMatchHistory(String puuid) {
        String matchHistoryUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?start=0&count=20&api_key=" + apiKey;

        List<String> matchIds = webClient.get()
                .uri(matchHistoryUrl, puuid)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();

        if (matchIds != null) {
            return matchIds;
        }

        return Collections.emptyList();
    }

    //매치아이디로 인게임 정보 가져오기
    public MatchDTO getMatchDetailsById(String matchId) {
        String matchDetailsUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/{matchId}?api_key=" + apiKey;

        MatchDTO matchDTO = webClient.get()
                .uri(matchDetailsUrl, matchId)
                .retrieve()
                .bodyToMono(MatchDTO.class)
                .block();


        return matchDTO;
    }


    //닉네임으로 소환사 정보 가져오기
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

    public String getSummonerInfoByNickname(String nickname) {
        String summonerUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{nickname}?api_key=" + apiKey;


        SummonerDTO summonerDTO = webClient.get()
                .uri(summonerUrl, nickname)
                .retrieve()
                .bodyToMono(SummonerDTO.class)
                .block();

        if (summonerDTO != null) {
            return summonerDTO.getName();
        }
        return "";
    }

    //닉네임으로 소환사 아이콘 주소 가져오기
    public int getSummonerIconByNickname(String nickname) {
        String summonerUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{nickname}?api_key=" + apiKey;

        SummonerDTO summonerDTO = webClient.get()
                .uri(summonerUrl, nickname)
                .retrieve()
                .bodyToMono(SummonerDTO.class)
                .block();

        if (summonerDTO != null) {

            return summonerDTO.getProfileIconId();
        }


        return 0;
    }

    //버전 가져오기
    public String getLatestDdragonVersion() {
        String versionUrl = "https://ddragon.leagueoflegends.com/api/versions.json";

        String[] versions = webClient.get()
                .uri(versionUrl)
                .retrieve()
                .bodyToMono(String[].class)
                .block();

        return versions != null ? versions[0] : null;
    }

    //아이콘 주소로 아이콘 이미지 가져오기
    public String getSummonerIconUrl(int iconId, String version) {
        return String.format("https://ddragon.leagueoflegends.com/cdn/%s/img/profileicon/%d.png", version, iconId);
    }

    //tier같은 정보

    public LeagueEntryDTO getSummonerTier(String summonerId) {
        String entriesUrl = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/{summonerId}?api_key=" + apiKey;

        List<LeagueEntryDTO> leagueEntryDTOs = webClient.get()
                .uri(entriesUrl, summonerId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<LeagueEntryDTO>>() {})
                .block();

        if (leagueEntryDTOs != null) {
            for (LeagueEntryDTO leagueEntryDTO : leagueEntryDTOs) {
                if ("RANKED_SOLO_5x5".equals(leagueEntryDTO.getQueueType())) {
                    return leagueEntryDTO;
                }
            }
        }

        return null;
    }

}
