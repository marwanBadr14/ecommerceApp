package com.gizasystems.purchasingservice.rest;

import com.gizasystems.purchasingservice.dto.PaymentDTO;
import com.gizasystems.purchasingservice.model.Payment;
import com.gizasystems.purchasingservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class PaymentController {
    PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/processPayment")
    public ResponseEntity<Payment> processPayment(@RequestBody PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setOrderId(paymentDTO.orderId());
        payment.setPaymentStatus(paymentDTO.paymentStatus());
        payment.setPaymentMethod(paymentDTO.paymentMethod());
        payment.setPaymentAmount(paymentDTO.paymentAmount());
        payment.setPaymentDate(LocalDateTime.now());

        // TODO: Update inventory

        return paymentService.add(payment);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return paymentService.findAll();
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Integer id) {
        return paymentService.findById(id);
    }

}
