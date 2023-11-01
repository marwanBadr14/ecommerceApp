package com.gizasystems.inventoryservice.dao;


import com.gizasystems.inventoryservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryDao extends JpaRepository<Product,Integer> {

    List<Product> findByCategory(String categoryName);

}
