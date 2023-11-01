package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.entity.Product;
import com.gizasystems.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all/{category}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String categoryName){
        return inventoryService.getProductByCategory(categoryName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id){
        return inventoryService.getProductById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return inventoryService.addProduct(product);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Product> editProductCategory(@PathVariable Integer id, @RequestBody Product product){
        return inventoryService.editProductById(id,product);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Product> deleteProductById(@PathVariable Integer id){
        return inventoryService.deleteProductById(id);
    }

    @PutMapping("/deduct")
    public ResponseEntity<Integer> deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity){
        return inventoryService.deductFromStock(id,quantity);
    }

}
