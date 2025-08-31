package com.jacek.nutriweek.dto.usda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private int totalHits;
    private int currentPage;
    private int totalPages;
    private List<ApiProduct> foods;
}
