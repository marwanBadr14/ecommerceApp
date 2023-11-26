package com.EcommerceApp.OrderService.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient("INVENTORY-SERVICE")
public interface InventoryServiceClient {
    @PutMapping("/inventory/deduct")
    ResponseEntity<Boolean> deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity);

    @GetMapping("/products/price/{id}")
    ResponseEntity<BigDecimal> getProductPrice(@PathVariable Integer id);

    @GetMapping("/products/name/{id}")
    ResponseEntity<String> getProductName(@PathVariable Integer id);

}
