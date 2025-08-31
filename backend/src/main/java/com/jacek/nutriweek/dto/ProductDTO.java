package com.jacek.nutriweek.dto;

import java.util.List;

public record ProductDTO (int fdcId,
                          String name,
                          List<NutrientDTO> nutrients){}
