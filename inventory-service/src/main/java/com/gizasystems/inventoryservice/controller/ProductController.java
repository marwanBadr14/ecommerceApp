package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.dto.ProductDto;
import com.gizasystems.inventoryservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
public class ProductController {
    // TODO: 11/14/2023 nice to have use constructor injection

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/price/{id}")
    public ResponseEntity<BigDecimal> getProductPrice(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getProductPrice(id));
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getProductName(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getProductName(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto){
        return ResponseEntity.ok(productService.addProduct(productDto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ProductDto> editProductCategory(@PathVariable Integer id, @RequestBody ProductDto productDto){
        return ResponseEntity.ok(productService.editProductById(id,productDto));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProductById(@PathVariable Integer id){
        productService.deleteProductById(id);
    }

}
