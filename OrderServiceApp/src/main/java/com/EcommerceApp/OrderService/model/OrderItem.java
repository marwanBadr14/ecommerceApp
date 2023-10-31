package com.EcommerceApp.OrderService.model;
import com.EcommerceApp.OrderService.key.OrderItemPK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Entity
@Table(name = "order_items")
@IdClass(OrderItemPK.class) // Specify the composite key class
@Data
@EqualsAndHashCode
public class OrderItem {
    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "item_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal itemPrice;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id") // This references the "order_id" column in the order_items table
    private Order order; // This establishes the relationship


    public OrderItem(int orderId, int productId, int quantity, BigDecimal itemPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public OrderItem() {
    }

}
