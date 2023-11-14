package com.gizasystems.purchasingservice.dao;

import com.gizasystems.purchasingservice.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    Purchase findByProductId(Integer productId);
}
