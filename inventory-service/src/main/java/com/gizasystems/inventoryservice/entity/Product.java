package com.gizasystems.inventoryservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;
    @Column(name = "product_name")
    private String name;
    @Column(name = "product_description")
    private String description;
    @JoinColumn(name = "category_id")
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH,
                        CascadeType.MERGE,CascadeType.REFRESH})
    private ProductCategory productCategory;
    @Column(name = "product_price")
    private double price;
    @Column(name = "product_quantity")
    private Integer quantity;

    public Product() {
    }

    public Product(String name, String description, ProductCategory productCategory, double price, Integer quantity) {
        this.name = name;
        this.description = description;
        this.productCategory = productCategory;
        this.price = price;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
