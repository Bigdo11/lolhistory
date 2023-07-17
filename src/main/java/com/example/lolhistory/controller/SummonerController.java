package com.example.lolhistory.controller;

import com.example.lolhistory.dto.*;
import com.example.lolhistory.service.SummonerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/summoner")
public class SummonerController {
    private final SummonerService summonerService;
    SummonerDTO summonerDTO = new SummonerDTO();

    @Autowired
    public SummonerController(SummonerService summonerService) {
        this.summonerService = summonerService;
    }
    @GetMapping("/")
    public String getSummonerInfo(@RequestParam("nickname") String nickname,String losses, String wins, String level, Model model) {

        //레벨
        String name = summonerService.getSummonerInfoByNickname(nickname); //진짜 닉네임 구하기 (logicalmoving -> Logical Moving)
        String summonerId = summonerService.getSummonerIdByNickname(name);
        int summonerLevel= summonerService.getSummonerLevelByNickname(name);

        //아이콘
        int iconId =summonerService.getSummonerIconByNickname(name);
        String version = summonerService.getLatestDdragonVersion();
        String iconUrl = summonerService.getSummonerIconUrl(iconId,version);

        //인게임 소환사들
        String puuid = summonerService.getPuuidBySummonerName(name);
        List<String> matchIds = summonerService.getSummonerMatchHistory(puuid);
        List <String> summonerNames = new ArrayList<>();

        //승리 패배
        int winCount = 0;
        int loseCount = 0;
        boolean userWin = false;
        String time = new String();
        List<String> times = new ArrayList<>();
        List<Boolean> userWins = new ArrayList<>();

        //날짜
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("M/dd");

        //


        //매치아이디로 팀 소환사 가져오기
        for (String matchId : matchIds) {
            MatchDTO matchDTO = summonerService.getMatchDetailsById(matchId);
            InfoDTO infoDTO = matchDTO.getInfo();
            List<ParticipantDTO> participants = infoDTO.getParticipants();

            for(ParticipantDTO participant : participants){
                String summonerName = participant.getSummonerName();
                //승리 몇번했는지
                boolean isWin = participant.isWin();
                if(summonerName.equals(name)){
                    userWin = participant.isWin();
                    timeStamp = infoDTO.getGameStartTimestamp();
                    time = sdf.format(timeStamp);
                    times.add(time);
                }
                summonerNames.add(summonerName);
                }
            userWins.add(userWin);
            if(userWin){
                winCount++;
            }
            else{
                loseCount++;
            }
        }


        //소환사들 5개씩 묶기
        List<List<String>> groupedSummonerNamesByFive = new ArrayList<>();
        for (int i = 0; i < summonerNames.size(); i += 5) {
            int endIndex = Math.min(i + 5, summonerNames.size());
            groupedSummonerNamesByFive.add(summonerNames.subList(i, endIndex));
        }
        //그 5개씩 묶은 소환사들을 2개로 묶기
        List<List<List<String>>> groupedSummonerNamesByTwo = new ArrayList<>();
        for (int i = 0; i < groupedSummonerNamesByFive.size(); i += 2) {
            int endIndex = Math.min(i + 2, groupedSummonerNamesByFive.size());
            groupedSummonerNamesByTwo.add(groupedSummonerNamesByFive.subList(i, endIndex));
        }

        //소환사들과 시간 묶기
        int minSize = Math.min(groupedSummonerNamesByTwo.size(), times.size());

        List<AbstractMap.SimpleEntry<List<List<String>>, String>> timesSummonersData =
                IntStream.range(0, minSize)
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>(groupedSummonerNamesByTwo.get(i), times.get(i)))
                        .collect(Collectors.toList());

        //
        LeagueEntryDTO leagueEntryDTO = summonerService.getSummonerTier(summonerId);



        model.addAttribute("summonerNames",groupedSummonerNamesByTwo);
        model.addAttribute("name", name);
        model.addAttribute("level",summonerLevel);
        model.addAttribute("iconUrl",iconUrl);
        model.addAttribute("winCount",winCount);
        model.addAttribute("loseCount",loseCount);
        model.addAttribute("userWins",userWins);
        model.addAttribute("times",times);
        model.addAttribute("timesSummonersData",timesSummonersData);
        if(leagueEntryDTO!=null){
            String tier = leagueEntryDTO.getTier();
            model.addAttribute("tier", tier);
            model.addAttribute("rank", leagueEntryDTO.getRank());
            model.addAttribute("tierUrl", "/images/tiers/" + tier.toLowerCase() + ".png");
        }
        else{
            model.addAttribute("tier", "UNRANKED");
            model.addAttribute("rank", "N/A");
            model.addAttribute("tierUrl", "/images/tiers/unranked.png");
        }

        return "summoner";
    }
}
