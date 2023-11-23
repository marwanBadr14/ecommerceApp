package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.dto.ProductDto;
import com.gizasystems.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/inventory")
public class InventoryController {

    // TODO el customer lw 3ayez yshof kol el products msh mafrood yshoof el quantity bardo

    InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        return ResponseEntity.ok(inventoryService.getAllProducts());
    }

    @GetMapping("/categories/{categoryName}")
    public ResponseEntity<List<ProductDto>> getProductByCategory(@PathVariable String categoryName){
        return ResponseEntity.ok(inventoryService.getProductByCategory(categoryName));
    }

    @PutMapping("/deduct")
    public void deductFromStock(@RequestParam Integer id, @RequestParam Integer quantity){
        inventoryService.deductFromStock(id,quantity);
    }

}
