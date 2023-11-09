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
    public void deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity);

    @GetMapping("/products/price/{id}")
    public BigDecimal getProductPrice(@PathVariable Integer id);

    @GetMapping("/products/name/{id}")
    public String getProductName(@PathVariable Integer id);

}
