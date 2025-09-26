package com.jacek.nutriweek.menu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacek.nutriweek.menu.dto.usda.ApiNutrient;
import com.jacek.nutriweek.menu.dto.usda.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;

@Slf4j
@Service
public class ProductService {
    private final boolean useDummyData = false;

    private final String apiUrl = "https://api.nal.usda.gov/fdc/v1";
    private final WebClient webClient;

    public ProductService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(1024 * 1024))
                .baseUrl(apiUrl)
                .build();
    }

    public ApiResponse searchProducts(String query, int page, int size) {
        String apiKey = System.getenv("USDA_API_KEY");

        try {
            ApiResponse response;
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

            response
                    .getFoods()
                    .forEach(food -> food.getNutrients().sort(Comparator.comparingInt(ApiNutrient::getRank)));

            return response;

        } catch (Exception e){
            log.error(e.getMessage());
            return new ApiResponse();
        }
    }
}
