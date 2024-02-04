package com.gizasystems.purchasingservice.service;

import com.gizasystems.purchasingservice.dao.PurchaseRepository;
import org.dto.PurchaseDTO;
import com.gizasystems.purchasingservice.exception.PurchaseNotFoundException;
import com.gizasystems.purchasingservice.helper.PurchaseMapper;
import com.gizasystems.purchasingservice.model.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;

    @Override
    public List<PurchaseDTO> findAll() {
        List<PurchaseDTO> purchaseDTOS = PurchaseMapper.convertFromEntityToDto(purchaseRepository.findAll());
        if (purchaseDTOS == null)
            throw new PurchaseNotFoundException("No purchases found");
        return purchaseDTOS;
    }

    @Override
    public PurchaseDTO findByProductId(Integer productId) {
        PurchaseDTO purchaseDTO = PurchaseMapper.convertFromEntityToDto(purchaseRepository.findByProductId(productId));
        if (purchaseDTO == null)
            throw new PurchaseNotFoundException("No purchase found with productId: " + productId);
        return purchaseDTO;
    }

    @Override
    public PurchaseDTO add(PurchaseDTO purchaseDTO) {
        Purchase savedPurchase = purchaseRepository.save(PurchaseMapper.convertFromDtoToEntity(purchaseDTO));
        return PurchaseMapper.convertFromEntityToDto(savedPurchase);
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
        return PurchaseMapper.convertFromEntityToDto(savedPurchase);
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
