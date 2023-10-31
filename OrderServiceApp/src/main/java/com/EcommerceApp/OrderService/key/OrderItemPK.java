package com.EcommerceApp.OrderService.key;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class OrderItemPK implements Serializable {
    private Integer orderId;
    private Integer productId;



    // No need to write explicit constructor or getter/setter methods
}
