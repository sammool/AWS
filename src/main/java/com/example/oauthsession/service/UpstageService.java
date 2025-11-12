package com.example.oauthsession.service;

import com.example.oauthsession.dto.UpstageRequestDto;
import com.example.oauthsession.dto.UpstageResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UpstageService {

    // WebConfig에서 정의한 upstageWebClient Bean을 주입받습니다.
    private final WebClient upstageWebClient;
    private final String apiPath; // "/v1/chat/completions"

    public UpstageService(WebClient upstageWebClient, @Value("${upstage.api.path}") String apiPath) {
        this.upstageWebClient = upstageWebClient;
        this.apiPath = apiPath;
    }

    /**
     * [핵심 메서드]
     * 사용자의 질문(프롬프트)을 받아 Upstage API를 호출하고, 답변 텍스트(String)를 반환합니다.
     * 이 메서드는 가장 일반적인 '요청-응답' 방식으로 동작합니다.
     *
     * @param modelName 사용할 모델 (예: "solar-pro2")
     * @param userPrompt 사용자 질문 텍스트
     * @return Mono<String> Upstage API의 최종 답변 텍스트
     */
    public Mono<String> getChatResponseText(String modelName, String userPrompt) {

        // 1. Upstage API 요청 형식에 맞는 Message 객체 생성
        UpstageRequestDto.Message userMessage = new UpstageRequestDto.Message("user", userPrompt);

        // 2. 요청 DTO 생성 (stream=false는 기본값이므로 설정할 필요 없음)
        UpstageRequestDto requestDto = new UpstageRequestDto(
                modelName,
                List.of(userMessage),
                false, // stream=false 명시적 설정
                null
        );

        System.out.println("--- Upstage API 호출 시작: " + modelName + " ---");

        return this.upstageWebClient.post()
                .uri(apiPath)
                .bodyValue(requestDto) // 요청 DTO를 JSON으로 변환
                .retrieve()
                // 응답 JSON을 DTO로 역직렬화
                .bodyToMono(UpstageResponseDto.class)
                .map(response -> {
                    // 3. 응답 DTO에서 답변 텍스트 추출 및 반환
                    if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                        return response.getChoices().get(0).getMessage().getContent();
                    }
                    return "API로부터 유효한 응답을 받지 못했습니다.";
                })
                .doOnError(error -> System.err.println("API 호출 실패: " + error.getMessage()))
                .onErrorResume(e -> {
                    // 예외 처리 (WebClient 통신 오류, 4xx/5xx 상태 코드 등)
                    return Mono.error(new RuntimeException("Upstage API 호출 및 처리 실패", e));
                });
    }
}