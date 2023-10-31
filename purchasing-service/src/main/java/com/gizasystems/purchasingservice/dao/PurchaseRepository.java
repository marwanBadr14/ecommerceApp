package com.gizasystems.purchasingservice.dao;

import com.gizasystems.purchasingservice.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

}
