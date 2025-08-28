package com.jacek.nutriweek.dto;

import com.jacek.nutriweek.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiSearchResponse {

    private int totalHits;
    private int currentPage;
    private int totalPages;
    private List<Product> foods;
}
