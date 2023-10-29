package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.entity.Product;
import com.gizasystems.inventoryservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        return productService.getAllProducts();
    }
}
