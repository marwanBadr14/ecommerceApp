package com.gizasystems.inventoryservice.dao;


import com.gizasystems.inventoryservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryDao extends JpaRepository<Product,Integer> {

    @Query(value = "SELECT p.* " +
            "FROM products p " +
            "JOIN product_categories pc ON p.category_id = pc.category_id " +
            "WHERE pc.category_name = :categoryName",nativeQuery = true)
    List<Product> findByCategory(String categoryName);

}
