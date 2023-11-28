package com.EcommerceApp.OrderService.feign;


import org.dto.PurchaseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("PURCHASING-SERVICE")

public interface PurchaseServiceIClient {
    @PostMapping("/purchases/increase-purchases")
    ResponseEntity<List<PurchaseDTO>> processPurchasesRequest(@RequestBody List<PurchaseDTO> purchaseDTOs);
}
