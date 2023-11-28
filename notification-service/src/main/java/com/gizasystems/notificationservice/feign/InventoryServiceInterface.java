package com.gizasystems.notificationservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient("INVENTORY-SERVICE")
public interface InventoryServiceInterface {

    @GetMapping("/products/price/{id}")
    public ResponseEntity<BigDecimal> getProductPrice(@PathVariable Integer id);

    @GetMapping("/products/name/{id}")
    public ResponseEntity<String> getProductName(@PathVariable Integer id);
}
