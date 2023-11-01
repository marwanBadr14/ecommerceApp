package com.gizasystems.purchasingservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "num_of_purchases")
    private Integer numOfPurchases;

    public Purchase() {
    }

    public Purchase(Integer productId, Integer numOfPurchases) {
        this.productId = productId;
        this.numOfPurchases = numOfPurchases;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getNumOfPurchases() {
        return numOfPurchases;
    }

    public void setNumOfPurchases(Integer numOfPurchases) {
        this.numOfPurchases = numOfPurchases;
    }
}
