package org.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;



public record PurchaseDTO(
        @NotNull(message = "productId cannot be null")
        Integer productId,
        @Min(value = 0, message = "quantity cannot be less than 0")
        Integer quantity
) {
}
