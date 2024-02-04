package com.gizasystems.purchasingservice.helper;

import org.dto.PurchaseDTO;
import com.gizasystems.purchasingservice.model.Purchase;

import java.util.List;

public class PurchaseMapper {

    public static Purchase convertFromDtoToEntity(PurchaseDTO purchaseDTO) {
        return new Purchase(purchaseDTO.productId(), purchaseDTO.quantity());
    }

    public static PurchaseDTO convertFromEntityToDto(Purchase purchase) {
        if (purchase == null)
            return null;
        return new PurchaseDTO(purchase.getProductId(), purchase.getNumOfPurchases());
    }

    public static List<PurchaseDTO> convertFromEntityToDto(List<Purchase> purchase) {
        if (purchase == null)
            return null;
        return purchase.stream().map(PurchaseMapper::convertFromEntityToDto).toList();
    }
}
