package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.common.TokenRateLimiter;
import com.jacek.nutriweek.menu.dto.usda.ApiNutrient;
import com.jacek.nutriweek.menu.dto.usda.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Comparator;

@Slf4j
@Service
public class ProductService {
    @Value("${app.usda.key}") String API_KEY;
    @Value("${app.usda.url}") String API_URL;

    private final WebClient webClient;
    private final TokenRateLimiter rateLimiter = new TokenRateLimiter(1000, 3600000);

    public ProductService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(1024 * 1024))
                .baseUrl(API_URL)
                .build();
    }

    public ApiResponse searchProducts(String query, int page, int size) {
        boolean requestAvailable = rateLimiter.tryConsume();

        if(requestAvailable) {
            try {
                ApiResponse response;
                response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/foods/search")
                                .queryParam("query", query)
                                .queryParam("pageSize", size)
                                .queryParam("pageNumber", page)
                                .queryParam("api_key", API_KEY)
                                .build())
                        .retrieve()
                        .bodyToMono(ApiResponse.class)
                        .block();

                if (response != null)
                    response
                            .foods()
                            .forEach(food -> food.getNutrients().sort(Comparator.comparingInt(ApiNutrient::getRank)));


                log.info("Products from USDA retrieved, {} tokens left", rateLimiter.tokens());

                return response;

            } catch (Exception e) {
                log.error("Failed fetching USDA products: {}", e.getMessage());
                return new ApiResponse(0, 0, 0, Collections.emptyList());
            }
        } else {
            return new ApiResponse(0, 0, 0, Collections.emptyList());
        }
    }
}
