package com.jacek.nutriweek.menu.dto.usda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


public record ApiResponse(int totalHits,
                          int currentPage,
                          int totalPages,
                          List<ApiProduct> foods) {}


