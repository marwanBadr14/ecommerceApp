package com.gizasystems.inventoryservice.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "product_categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;
    @Column(name = "category_name")
    private String name;
//    @OneToMany(mappedBy = "category",
//    cascade = {CascadeType.PERSIST,CascadeType.DETACH,
//                CascadeType.MERGE,CascadeType.REFRESH})
//    @JsonIgnore

    @ElementCollection
    private List<Integer> productsId;

    public Category() {
    }

    public Category(String name, List<Integer> productsId) {
        this.name = name;
        this.productsId = productsId;
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

    public List<Integer> getProductsId() {
        return productsId;
    }

    public void setProductsId(List<Integer> productsId) {
        this.productsId = productsId;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", products=" + productsId +
                '}';
    }
}
