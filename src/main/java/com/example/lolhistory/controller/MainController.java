package com.example.lolhistory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String mainPage() {
        return "mainPage";
    }

    @PostMapping("/searchNam e")
    public String searchName(){
        return "redirect:/소환사 페이지";
    }
}
