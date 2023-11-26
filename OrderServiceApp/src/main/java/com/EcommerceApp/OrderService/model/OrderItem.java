package com.EcommerceApp.OrderService.model;
import com.EcommerceApp.OrderService.dto.OrderItemPK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer orderItemId;

    @Column(name = "order_id")
        private Integer orderId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "item_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal itemPrice;


    @ManyToOne
    @JoinColumn(name = "order_id", insertable=false, updatable=false)
    private Order order;

    public OrderItem(int orderId, int productId, int quantity, BigDecimal itemPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public OrderItem() {
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                ", order=" + order +
                '}';
    }
}
