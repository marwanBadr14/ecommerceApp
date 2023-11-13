package com.gizasystems.inventoryservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CategoryDto {

    Integer id;
    String name;
}
