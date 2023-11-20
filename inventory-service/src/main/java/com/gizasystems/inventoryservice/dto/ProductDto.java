package com.gizasystems.inventoryservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
public record ProductDto(Integer id, String name,String description,Integer categoryId,
                         BigDecimal price,Integer quantity, String imageUrl) {
}
