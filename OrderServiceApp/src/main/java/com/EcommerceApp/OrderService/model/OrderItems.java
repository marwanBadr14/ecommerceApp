package com.EcommerceApp.OrderService.model;
import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name = "order_items")
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "item_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal itemPrice;


    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", insertable = false, updatable = false)
    private Order order;

    public OrderItems(int orderId, int productId, int quantity, BigDecimal itemPrice, Order order) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.order = order;
    }

    public OrderItems() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                ", order=" + order +
                '}';
    }
}
