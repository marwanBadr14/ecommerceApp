package com.EcommerceApp.OrderService.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("INVENTORY-SERVICE")
public interface OrderInterface {
    @PutMapping("/inventory/deduct")
    public ResponseEntity<Integer> deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity);
}
