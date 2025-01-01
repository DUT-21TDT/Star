package com.pbl.star.services.external.impl;

import com.pbl.star.services.external.PredictCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PredictCallServiceImpl implements PredictCallService {

    @Value("${predict.server.url}")
    private String predictUrl;

    private final WebClient webClient;

    @Override
    public void callPredictAsync(String postId) {

        Map<String, String> reqBody = Map.of("post_id", postId);

        webClient.post()
                .uri(predictUrl + "/predict")
                .bodyValue(reqBody) // Truyền body
                .retrieve()
                .bodyToMono(String.class) // Không cần xử lý response
                .subscribe(
                        response -> System.out.println("Request successful."),
                        error -> System.err.println("Error occurred: " + error.getMessage())
                );
    }
}
