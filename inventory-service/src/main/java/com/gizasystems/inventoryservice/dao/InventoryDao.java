package com.gizasystems.inventoryservice.dao;


import com.gizasystems.inventoryservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryDao extends JpaRepository<Product,Integer> {

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.categoryId IN (SELECT c.id FROM Category c WHERE c.name = :categoryName)")
    List<Product> findByCategory(String categoryName);

}
