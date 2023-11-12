package com.gizasystems.purchasingservice.service;

import com.gizasystems.purchasingservice.dao.PurchaseRepository;
import com.gizasystems.purchasingservice.dto.PurchaseDTO;
import com.gizasystems.purchasingservice.exception.PurchaseNotFoundException;
import com.gizasystems.purchasingservice.model.Purchase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public List<PurchaseDTO> findAll() {
        List<PurchaseDTO> purchaseDTOS =  PurchaseDTO.from(purchaseRepository.findAll());
        if (purchaseDTOS == null)
            throw new PurchaseNotFoundException("No purchases found");
        return purchaseDTOS;
    }

    @Override
    public PurchaseDTO findByProductId(Integer productId) {
        PurchaseDTO purchaseDTO = PurchaseDTO.from(purchaseRepository.findByProductId(productId));
        if (purchaseDTO == null)
            throw new PurchaseNotFoundException("No purchase found with productId: " + productId);
        return purchaseDTO;
    }

    @Override
    public PurchaseDTO add(PurchaseDTO purchaseDTO) {
        Purchase savedPurchase = purchaseRepository.save(Purchase.from(purchaseDTO));
        return PurchaseDTO.from(savedPurchase);
    }
    @Override
    public PurchaseDTO update(PurchaseDTO purchaseDTO) {
        Purchase dbPurchase = purchaseRepository.findByProductId(purchaseDTO.productId());
        if (dbPurchase == null)
            throw new PurchaseNotFoundException("No purchase found with productId: " + purchaseDTO.productId());
        return update(purchaseDTO, dbPurchase);
    }
    public PurchaseDTO update(PurchaseDTO purchaseDTO, Purchase dbPurchase) {
        dbPurchase.increaseNumOfPurchases(purchaseDTO.quantity());
        Purchase savedPurchase = purchaseRepository.save(dbPurchase);
        return PurchaseDTO.from(savedPurchase);
    }

    @Override
    public PurchaseDTO addOrUpdate(PurchaseDTO purchaseDTO) {
        Purchase dbPurchase = purchaseRepository.findByProductId(purchaseDTO.productId());
        if (dbPurchase == null) {
            return add(purchaseDTO);
        }
        return update(purchaseDTO, dbPurchase);
    }
}
