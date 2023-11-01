package com.EcommerceApp.OrderService.feign;


import com.gizasystems.purchasingservice.dto.PurchaseDTO;
import com.gizasystems.purchasingservice.model.Purchase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient("INVENTORY-SERVICE")
public interface InventoryServiceClient {
    @PutMapping("/inventory/deduct")
    ResponseEntity<Integer> deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity);

    @GetMapping("/inventory/price/{id}")
    ResponseEntity<BigDecimal> getProductPrice(@PathVariable Integer id);

    @PostMapping("/increase-purchases")
    ResponseEntity<List<Purchase>> processPurchasesRequest(@RequestBody List<PurchaseDTO> purchaseDTOs);
}
