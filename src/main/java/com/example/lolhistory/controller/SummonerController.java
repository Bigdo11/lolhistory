package com.example.lolhistory.controller;

import com.example.lolhistory.dto.SummonerDTO;
import com.example.lolhistory.service.SummonerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        String summonerId = summonerService.getSummonerIdByNickname(nickname);
        int summonerLevel= summonerService.getSummonerLevelByNickname(nickname);


        model.addAttribute("nickname", nickname);
        model.addAttribute("level",summonerLevel);
        return "summoner";
    }
}
