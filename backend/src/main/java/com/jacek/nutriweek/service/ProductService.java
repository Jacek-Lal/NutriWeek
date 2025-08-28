package com.jacek.nutriweek.service;

import com.jacek.nutriweek.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final String apiUrl = "https://api.nal.usda.gov/fdc/v1";
    private final WebClient webClient;

    public ProductService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public List<Product> searchProducts(String query, int page, int size) {
        String apiKey = System.getenv("USDA_API_KEY");

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/foods/search")
                            .queryParam("query", query)
                            .queryParam("pageSize", size)
                            .queryParam("pageNumber", page)
                            .queryParam("api_key", apiKey)
                            .build()
                    )
                    .retrieve()
                    .bodyToFlux(Product.class)
                    .collectList()
                    .block();

        } catch (Exception e){
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }
}
