package com.EcommerceApp.OrderService.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient("INVENTORY-SERVICE")
public interface InventoryServiceClient {
    @PutMapping("/inventory/deduct")
    void deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity);

    @GetMapping("/products/price/{id}")
    BigDecimal getProductPrice(@PathVariable Integer id);

    @GetMapping("/products/name/{id}")
    String getProductName(@PathVariable Integer id);

}
