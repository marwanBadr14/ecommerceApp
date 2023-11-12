package com.gizasystems.purchasingservice.rest;

import com.gizasystems.purchasingservice.dto.PurchaseDTO;
import com.gizasystems.purchasingservice.exception.PurchaseNotFoundException;
import com.gizasystems.purchasingservice.model.Purchase;
import com.gizasystems.purchasingservice.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/purchases")
@Validated
public class PurchaseController {
    PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/increase-purchases")
    public ResponseEntity<List<PurchaseDTO>> processPurchasesRequest(@RequestBody List<PurchaseDTO> purchaseDTOs) {
        List<PurchaseDTO> ResPurchaseDTOs = new ArrayList<>();

        for (PurchaseDTO purchaseDTO : purchaseDTOs) {
            ResponseEntity<PurchaseDTO> responsePurchase = processPurchaseRequest(purchaseDTO);
            if (!responsePurchase.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.internalServerError().body(ResPurchaseDTOs);
            }
            ResPurchaseDTOs.add(responsePurchase.getBody());
        }
        return ResponseEntity.ok(ResPurchaseDTOs);
    }

    @PostMapping("/increase-purchase")
    public ResponseEntity<PurchaseDTO> processPurchaseRequest(@Valid @RequestBody PurchaseDTO newPurchaseDTO) {
        try {
            PurchaseDTO purchaseDTO = purchaseService.addOrUpdate(newPurchaseDTO);
            return ResponseEntity.ok(purchaseDTO);
        } catch (PurchaseNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getAllPurchases() {
        List<PurchaseDTO> purchaseDTOS;
        try {
            purchaseDTOS = purchaseService.findAll();
            return ResponseEntity.ok(purchaseDTOS);
        } catch (PurchaseNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<PurchaseDTO> getPurchaseByProductId(@PathVariable Integer productId) {
        PurchaseDTO purchaseDTO;
        try {
            purchaseDTO = purchaseService.findByProductId(productId);
            return ResponseEntity.ok(purchaseDTO);
        } catch (PurchaseNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        Optional<FieldError> err = Optional.ofNullable(ex.getBindingResult().getFieldError());
        return ResponseEntity.badRequest().body(err.map(FieldError::getDefaultMessage).orElse("Invalid input"));
    }

}
