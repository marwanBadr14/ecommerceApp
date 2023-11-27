package com.EcommerceApp.OrderService.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
public class OrderItemPK implements Serializable {
    private Integer orderId;
    private Integer productId;

}
