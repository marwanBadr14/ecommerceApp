package com.gizasystems.purchasingservice.dto;

import com.gizasystems.purchasingservice.model.Purchase;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PurchaseDTO(
        @NotNull(message = "productId cannot be null")
        Integer productId,
        @Min(value = 0, message = "quantity cannot be less than 0")
        Integer quantity
) {
    public static PurchaseDTO from(Purchase purchase) {
        if (purchase == null)
            return null;
        return new PurchaseDTO(purchase.getProductId(), purchase.getNumOfPurchases());
    }

    public static List<PurchaseDTO> from(List<Purchase> purchase) {
        if (purchase == null)
            return null;
        return purchase.stream().map(PurchaseDTO::from).toList();
    }
}
