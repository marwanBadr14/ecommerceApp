package com.gizasystems.purchasingservice.dto;

public record PaymentDTO(
        Integer orderId,
        String paymentStatus,
        String paymentMethod,
        Double paymentAmount
) {
}
