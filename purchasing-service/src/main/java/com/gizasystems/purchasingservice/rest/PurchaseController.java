package com.gizasystems.purchasingservice.rest;

import com.gizasystems.purchasingservice.dto.PurchaseDTO;
import com.gizasystems.purchasingservice.model.Purchase;
import com.gizasystems.purchasingservice.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/")
    public String sayHello() {
        return "Hello world!";
    }


    @PostMapping("/increase-purchases")
    public ResponseEntity<List<Purchase>> processPurchasesRequest(@RequestBody List<PurchaseDTO> purchaseDTOs) {
        List<Purchase> purchases = new ArrayList<>();

        for (PurchaseDTO purchaseDTO : purchaseDTOs) {
            ResponseEntity<Purchase> responsePurchase = processPurchaseRequest(purchaseDTO);
            if (!responsePurchase.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.internalServerError().body(purchases);
            }
            purchases.add(responsePurchase.getBody());
        }
        return ResponseEntity.ok(purchases);
    }

    @PostMapping("/increase-purchase")
    public ResponseEntity<Purchase> processPurchaseRequest(@RequestBody PurchaseDTO purchaseDTO) {

        Integer productId = purchaseDTO.productId();

        Integer quantity = purchaseDTO.quantity();


        ResponseEntity<Purchase> responsePurchase = purchaseService.findById(productId);
        Purchase purchase = responsePurchase.getBody();

        if (purchase == null) {
            purchase = new Purchase();
        }

        if (responsePurchase.getStatusCode() != HttpStatus.OK) {
            purchase.setProductId(productId);
            purchase.setNumOfPurchases(quantity);
            return purchaseService.add(purchase);
        } else {
            purchase.increaseNumOfPurchases(quantity);
            return purchaseService.update(purchase);
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
