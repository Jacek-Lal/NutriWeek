package com.jacek.nutriweek.menu.dto.usda;

import java.util.List;

public record ApiResponse(int totalHits,
                          int currentPage,
                          int totalPages,
                          List<ApiProduct> foods) {}


