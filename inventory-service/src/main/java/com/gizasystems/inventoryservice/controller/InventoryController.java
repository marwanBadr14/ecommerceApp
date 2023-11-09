package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.model.Product;
import com.gizasystems.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;
    @GetMapping("/all")
    public List<Product> getAllProducts(){
        return inventoryService.getAllProducts();
    }

    @GetMapping("/categories/{categoryName}")
    public List<Product> getProductByCategory(@PathVariable String categoryName){
        return inventoryService.getProductByCategory(categoryName);
    }

    @PutMapping("/deduct")
    public void deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity){
        inventoryService.deductFromStock(id,quantity);
    }

}
