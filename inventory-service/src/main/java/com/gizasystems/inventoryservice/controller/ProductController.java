package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.dto.ProductDto;
import com.gizasystems.inventoryservice.model.Product;
import com.gizasystems.inventoryservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id){
        return productService.getProductById(id);
    }

    @GetMapping("/price/{id}")
    public BigDecimal getProductPrice(@PathVariable Integer id){
        return productService.getProductPrice(id);
    }

    @GetMapping("/name/{id}")
    public String getProductName(@PathVariable Integer id){
        return productService.getProductName(id);
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody ProductDto productDto){
        return productService.addProduct(productDto);
    }

    @PutMapping("/edit/{id}")
    public Product editProductCategory(@PathVariable Integer id, @RequestBody ProductDto productDto){
        return productService.editProductById(id,productDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProductById(@PathVariable Integer id){
        productService.deleteProductById(id);
    }

}
