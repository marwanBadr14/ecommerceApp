package com.gizasystems.purchasingservice.service;

import com.gizasystems.purchasingservice.dto.PurchaseDTO;

import java.util.List;

public interface PurchaseService {
    List<PurchaseDTO> findAll();
    PurchaseDTO findByProductId(Integer productId);
    PurchaseDTO add(PurchaseDTO purchaseDTO);
    PurchaseDTO update(PurchaseDTO purchaseDTO);
    PurchaseDTO addOrUpdate(PurchaseDTO purchaseDTO);
}
