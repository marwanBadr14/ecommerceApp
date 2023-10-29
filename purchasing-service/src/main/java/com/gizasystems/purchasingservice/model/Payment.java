package com.gizasystems.purchasingservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "payment_amount")
    private Double paymentAmount;
    @Column(name = "payment_method")
    private String paymentMethod;

    public Payment() {}
    public Payment(Integer orderId, LocalDateTime paymentDate, String paymentStatus, Double paymentAmount, String paymentMethod) {
        this.orderId = orderId;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
    }

    public Integer getPaymentId() {
        return id;
    }

    public void setPaymentId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
