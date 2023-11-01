package com.EcommerceApp.OrderService.feign;


import com.gizasystems.purchasingservice.dto.PurchaseDTO;
import com.gizasystems.purchasingservice.model.Purchase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("PURCHASING-SERVICE")

public interface PurchaseServiceIClient {
    @PostMapping("/increase-purchases")
    ResponseEntity<List<Purchase>> processPurchasesRequest(@RequestBody List<PurchaseDTO> purchaseDTOs);
}
