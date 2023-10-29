package com.gizasystems.purchasingservice.service;

import com.gizasystems.purchasingservice.model.Payment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {
    ResponseEntity<List<Payment>> findAll();
    ResponseEntity<Payment> findById(Integer id);
    ResponseEntity<Payment> add(Payment payment);
    ResponseEntity<String> deleteById(Integer id);
}
