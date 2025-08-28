package com.jacek.nutriweek.service;

import com.jacek.nutriweek.dto.ApiSearchResponse;
import com.jacek.nutriweek.model.Nutrient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Comparator;

@Slf4j
@Service
public class ProductService {

    private final String apiUrl = "https://api.nal.usda.gov/fdc/v1";
    private final WebClient webClient;

    public ProductService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(1024 * 1024))
                .baseUrl(apiUrl)
                .build();
    }

    public ApiSearchResponse searchProducts(String query, int page, int size) {
        String apiKey = System.getenv("USDA_API_KEY");

        try {
            ApiSearchResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/foods/search")
                            .queryParam("query", query)
                            .queryParam("pageSize", size)
                            .queryParam("pageNumber", page)
                            .queryParam("api_key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(ApiSearchResponse.class)
                    .block();

            response
                    .getFoods()
                    .forEach(food -> food.getFoodNutrients().sort(Comparator.comparingInt(Nutrient::getRank)));


            return response;

        } catch (Exception e){
            log.error(e.getMessage());
            return new ApiSearchResponse();
        }
    }
}
