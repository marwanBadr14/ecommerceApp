package com.gizasystems.purchasingservice.service;

import com.gizasystems.purchasingservice.model.Purchase;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PurchaseService {
    ResponseEntity<List<Purchase>> findAll();
    ResponseEntity<Purchase> findById(Integer productId);
    ResponseEntity<Purchase> add(Purchase purchase);
    ResponseEntity<Purchase> update(Purchase purchase);
}
