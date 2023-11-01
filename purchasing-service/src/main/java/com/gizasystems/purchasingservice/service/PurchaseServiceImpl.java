package com.gizasystems.purchasingservice.service;

import com.gizasystems.purchasingservice.dao.PurchaseRepository;
import com.gizasystems.purchasingservice.model.Purchase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Purchase>> findAll() {
        try {
            return new ResponseEntity<>(purchaseRepository.findAll(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Purchase> findById(Integer id) {
        try {
            return new ResponseEntity<>(purchaseRepository.findById(id).orElseThrow(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Purchase(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Purchase> add(Purchase purchase) {
        try {
            Purchase savedPurchase = purchaseRepository.save(purchase);
            return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(new Purchase(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Purchase> update(Purchase purchase) {
        try {
            Purchase savedPurchase = purchaseRepository.save(purchase);
            return new ResponseEntity<>(savedPurchase, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Purchase(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
