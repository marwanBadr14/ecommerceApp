package com.EcommerceApp.OrderService.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient("INVENTORY-SERVICE")
public interface OrderInterface {
    @PutMapping("/inventory/deduct")
    public ResponseEntity<Integer> deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity);

    @GetMapping("/products/price/{id}")
    public ResponseEntity<BigDecimal> getProductPrice(@PathVariable Integer id);

    @GetMapping("/products/name/{id}")
    public ResponseEntity<String> getProductName(@PathVariable Integer id);
}
