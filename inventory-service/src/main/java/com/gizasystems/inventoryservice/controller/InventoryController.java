package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.entity.Product;
import com.gizasystems.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        return inventoryService.getAllProducts();
    }

    @GetMapping("/categories/{categoryName}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String categoryName){
        return inventoryService.getProductByCategory(categoryName);
    }


    @PutMapping("/deduct")
    public ResponseEntity<Integer> deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity){
        return inventoryService.deductFromStock(id,quantity);
    }

}
