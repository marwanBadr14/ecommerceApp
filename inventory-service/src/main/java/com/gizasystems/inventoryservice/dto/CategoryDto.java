package com.gizasystems.inventoryservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CategoryDto {
    // TODO: 11/14/2023 you can make it a record instead of class
    Integer id;
    String name;
}
