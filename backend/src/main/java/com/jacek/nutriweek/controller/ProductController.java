package com.jacek.nutriweek.controller;

import com.jacek.nutriweek.dto.ApiResponse;
import com.jacek.nutriweek.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/products")
    public ResponseEntity<ApiResponse> getProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "25") int size,
                                                   @RequestParam(value = "query") String query){

        return ResponseEntity.ok().body(productService.searchProducts(query, page, size));
    }
}
