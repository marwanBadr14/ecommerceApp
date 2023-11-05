package com.EcommerceApp.OrderService.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode
@Data
public class OrderItemPK implements Serializable {
    private Integer orderId;
    private Integer productId;


}
