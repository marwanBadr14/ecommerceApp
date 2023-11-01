package com.EcommerceApp.OrderService.key;

import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode
public class OrderItemPK implements Serializable {
    private Integer orderId;
    private Integer productId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
