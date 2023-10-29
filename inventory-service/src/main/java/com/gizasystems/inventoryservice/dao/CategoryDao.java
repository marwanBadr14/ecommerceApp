package com.gizasystems.inventoryservice.dao;


import com.gizasystems.inventoryservice.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<ProductCategory,Integer> {
}
