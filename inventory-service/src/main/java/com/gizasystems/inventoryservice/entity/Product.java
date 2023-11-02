package com.gizasystems.inventoryservice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

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
//    @JoinColumn(name = "category_id")
//    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH,
//                        CascadeType.MERGE,CascadeType.REFRESH})
//    private Category category;
    @Column(name = "category_id")
    private Integer categoryId;
    @Column(name = "product_price")
    private BigDecimal price;
    @Column(name = "product_quantity")
    private Integer quantity;

    public Product() {
    }

    public Product(String name, String description, Integer categoryId, BigDecimal price, Integer quantity) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.quantity = quantity;
    }

    public void setId(Integer id) {
        this.id = id;
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

//    public Category getCategory() {
//        return category;
//    }
//
//    public void setCategory(Category category) {
//        this.category = category;
//    }


    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
 //               ", category=" + category +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
