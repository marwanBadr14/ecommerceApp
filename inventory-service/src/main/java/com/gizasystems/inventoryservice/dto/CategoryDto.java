package com.gizasystems.inventoryservice.dto;

import lombok.Builder;

@Builder
public record CategoryDto(Integer id, String name) {

}
