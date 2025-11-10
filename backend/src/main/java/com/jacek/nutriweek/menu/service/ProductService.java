package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.common.TokenRateLimiter;
import com.jacek.nutriweek.menu.dto.ProductDTO;
import com.jacek.nutriweek.menu.dto.usda.ApiResponse;
import com.jacek.nutriweek.menu.entity.Product;
import com.jacek.nutriweek.menu.mapper.ProductMapper;
import com.jacek.nutriweek.menu.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ProductService {
    private final String API_KEY;
    private final WebClient webClient;
    private final TokenRateLimiter rateLimiter = new TokenRateLimiter(1000, 3600000);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper,
                          @Value("${app.usda.key}") String API_KEY,
                          @Value("${app.usda.url}") String API_URL,
                          WebClient.Builder webClientBuilder){

        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.API_KEY = API_KEY;
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(1024 * 1024))
                .baseUrl(API_URL)
                .build();
    }

    public Page<ProductDTO> searchProducts(String query, int page, int size) {
        boolean requestAvailable = rateLimiter.tryConsume();

        if(requestAvailable) {
            try {
                ApiResponse response = webClient.get()
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

                log.info("Successfull USDA call, {} tokens left", rateLimiter.tokens());

                if(response == null){
                    log.info("No USDA products found for query {}", query);
                    return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);}

                List<ProductDTO> products = productMapper.fromApiToProductDtoList(response.foods());
                log.info("Returning {} products for query {}", products.size(), query);
                return new PageImpl<>(products, PageRequest.of(page, size), response.totalHits());

            } catch (Exception e) {
                log.error("Failed fetching USDA products: {}", e.getMessage());
                return getDatabaseProducts(query, page, size);
            }
        } else {
            log.info("No USDA API tokens left, fetching products from database");
            return getDatabaseProducts(query, page, size);
        }
    }

    private Page<ProductDTO> getDatabaseProducts(String query, int page, int size){
        String cleaned = query.replace("\"", "");
        log.info("Searching for term {}", cleaned);
        Page<Product> products = productRepository.findByQuery(cleaned, PageRequest.of(page, size));
        List<ProductDTO> mappedProducts = productMapper.toDtoList(products.getContent());
        return new PageImpl<>(mappedProducts, products.getPageable(), products.getTotalElements());
    }
}
