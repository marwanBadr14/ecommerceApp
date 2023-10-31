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
    @GetMapping("/all/{companyId}")
    public ResponseEntity<List<Product>> getAllCompanyProduct(@PathVariable Integer companyId){
        return inventoryService.getAllCompanyProducts(companyId);
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

}
