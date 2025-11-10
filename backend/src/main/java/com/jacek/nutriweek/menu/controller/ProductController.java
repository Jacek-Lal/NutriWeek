package com.jacek.nutriweek.menu.controller;

import com.jacek.nutriweek.menu.dto.ProductDTO;
import com.jacek.nutriweek.menu.dto.usda.ApiResponse;
import com.jacek.nutriweek.menu.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/products")
    public ResponseEntity<Page<ProductDTO>> getProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "25") int size,
                                                        @RequestParam(value = "query") String query){

        return ResponseEntity.ok().body(productService.searchProducts(query, page, size));
    }
}
