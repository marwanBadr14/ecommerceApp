package com.gizasystems.purchasingservice.rest;

import com.gizasystems.purchasingservice.model.Purchase;
import com.gizasystems.purchasingservice.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PurchaseController {
    PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/increase-purchase")
    public ResponseEntity<Purchase> processPurchase(@RequestBody Purchase purchaseDTO) {
        Integer productId = purchaseDTO.getProductId();
        ResponseEntity<Purchase> responsePurchase = purchaseService.findById(productId);
        Purchase purchase = responsePurchase.getBody();
        if (purchase == null) {
            purchase = new Purchase();
        }
        try {
            if (responsePurchase.getStatusCode() != HttpStatus.OK) {
                purchase.setProductId(productId);
                purchase.setNumOfPurchases(1);
                return purchaseService.add(purchase);
            } else {
                purchase.setNumOfPurchases(purchase.getNumOfPurchases() + 1);
                return purchaseService.update(purchase);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new Purchase());
        }
    }

    @GetMapping("/purchases")
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        return purchaseService.findAll();
    }

    @GetMapping("/purchases/{productId}")
    public ResponseEntity<Purchase> getPurchaseByProductId(@PathVariable Integer productId) {
        return purchaseService.findById(productId);
    }

}
