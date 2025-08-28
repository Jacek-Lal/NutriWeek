package com.jacek.nutriweek.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacek.nutriweek.dto.ApiSearchResponse;
import com.jacek.nutriweek.model.Nutrient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

@Slf4j
@Service
public class ProductService {
    private final boolean useDummyData = true;

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
            ApiSearchResponse response;
            if(!useDummyData){
                response = webClient.get()
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
            } else {
                ObjectMapper mapper = new ObjectMapper();
                ClassPathResource resource = new ClassPathResource("dummy.json");
                response = mapper.readValue(
                        resource.getInputStream(),
                        ApiSearchResponse.class
                );
            }

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
