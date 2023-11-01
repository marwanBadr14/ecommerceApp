package com.gizasystems.inventoryservice.dao;


import com.gizasystems.inventoryservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryDao extends JpaRepository<Product,Integer> {

    @Query(value = "SELECT p.*\n" +
            "FROM products p\n" +
            "JOIN products_categories pc ON p.category_id = pc.category_id\n" +
            "WHERE pc.category_name = 'YourDesiredCategoryName';",nativeQuery = true)
    List<Product> findByCategory(String categoryName);

}
