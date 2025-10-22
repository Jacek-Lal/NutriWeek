package com.jacek.nutriweek.menu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacek.nutriweek.menu.dto.usda.ApiNutrient;
import com.jacek.nutriweek.menu.dto.usda.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Comparator;

@Slf4j
@Service
public class ProductService {
    private final boolean useDummyData = false;
    @Value("${app.api.key}") String API_KEY;
    private final String API_URL = "https://api.nal.usda.gov/fdc/v1";
    private final WebClient webClient;

    public ProductService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(1024 * 1024))
                .baseUrl(API_URL)
                .build();
    }

    public ApiResponse searchProducts(String query, int page, int size) {

        try {
            ApiResponse response;
            if(!useDummyData){
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
            } else {
                ObjectMapper mapper = new ObjectMapper();
                ClassPathResource resource = new ClassPathResource("dummy.json");
                response = mapper.readValue(
                        resource.getInputStream(),
                        ApiResponse.class
                );
            }

            if(response != null)
                response
                    .foods()
                    .forEach(food -> food.getNutrients().sort(Comparator.comparingInt(ApiNutrient::getRank)));

            return response;

        } catch (Exception e){
            log.error(e.getMessage());
            return new ApiResponse(0 ,0, 0, Collections.emptyList());
        }
    }
}
