package com.gizasystems.inventoryservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record CategoryDto(Integer id, String name) {

}
