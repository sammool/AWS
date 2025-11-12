package com.example.oauthsession.controller;

import com.example.oauthsession.service.UpstageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UpstageService upstageService;

    @GetMapping("/")
    public String mainPage(){
        return "main";
    }

    @GetMapping("/chat")
    @ResponseBody
    public Mono<String> getResponse(@RequestParam String prompt){
        String model = "solar-pro2";
        return upstageService.getChatResponseText(model, prompt);
    }
}
