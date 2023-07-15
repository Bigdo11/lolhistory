package com.example.lolhistory.controller;

import com.example.lolhistory.dto.InfoDTO;
import com.example.lolhistory.dto.MatchDTO;
import com.example.lolhistory.dto.ParticipantDTO;
import com.example.lolhistory.dto.SummonerDTO;
import com.example.lolhistory.service.SummonerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        List<Boolean> userWins = new ArrayList<>();
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
                }
                summonerNames.add(summonerName);
                System.out.println(summonerNames);
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


        model.addAttribute("summonerNames",groupedSummonerNamesByTwo);
        model.addAttribute("name", name);
        model.addAttribute("level",summonerLevel);
        model.addAttribute("iconUrl",iconUrl);
        model.addAttribute("winCount",winCount);
        model.addAttribute("loseCount",loseCount);
        model.addAttribute("userWins",userWins);
        return "summoner";
    }
}
