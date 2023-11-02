package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.entity.Product;
import com.gizasystems.inventoryservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id){
        return productService.getProductById(id);
    }

    @GetMapping("/price/{id}")
    public ResponseEntity<BigDecimal> getProductPrice(@PathVariable Integer id){
        return productService.getProductPrice(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Product> editProductCategory(@PathVariable Integer id, @RequestBody Product product){
        return productService.editProductById(id,product);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteProductById(@PathVariable Integer id){
        return productService.deleteProductById(id);
    }

}
