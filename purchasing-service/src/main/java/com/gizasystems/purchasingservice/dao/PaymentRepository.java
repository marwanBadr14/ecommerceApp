package com.gizasystems.purchasingservice.dao;

import com.gizasystems.purchasingservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
