package com.gizasystems.purchasingservice.service;

import com.gizasystems.purchasingservice.dao.PaymentRepository;
import com.gizasystems.purchasingservice.model.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ResponseEntity<List<Payment>> findAll() {
        try {
            return new ResponseEntity<>(paymentRepository.findAll(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Payment> findById(Integer id) {
        try {
            return new ResponseEntity<>(paymentRepository.findById(id).orElseThrow(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Payment(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Payment> add(Payment payment) {
        try {
            Payment savedPayment = paymentRepository.save(payment);
            return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Payment(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteById(Integer id) {
        try {
            paymentRepository.deleteById(id);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }
}
