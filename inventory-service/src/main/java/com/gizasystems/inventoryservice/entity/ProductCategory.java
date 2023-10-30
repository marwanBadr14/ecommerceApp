package com.gizasystems.inventoryservice.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "product_categories")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer CategoryId;
    @Column(name = "category_name")
    private String categoryName;
    @OneToMany(mappedBy = "productCategory",
    cascade = {CascadeType.PERSIST,CascadeType.DETACH,
                CascadeType.MERGE,CascadeType.REFRESH})
    private List<Product> products;

    public ProductCategory() {
    }

    public ProductCategory(String categoryName, List<Product> products) {
        this.categoryName = categoryName;
        this.products = products;
    }

    public Integer getCategoryId() {
        return CategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
