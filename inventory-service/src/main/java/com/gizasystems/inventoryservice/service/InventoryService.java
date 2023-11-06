package com.gizasystems.inventoryservice.service;

import com.gizasystems.inventoryservice.dao.InventoryDao;
import com.gizasystems.inventoryservice.entity.Category;
import com.gizasystems.inventoryservice.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    InventoryDao inventoryDao;

    // Retrieve all products in the inventory
    public ResponseEntity<List<Product>> getAllProducts() {
        try{
            return new ResponseEntity<>(inventoryDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Couldn't retrieve all products");
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }


    // Retrieve a list of products that belong to the same category
    public ResponseEntity<List<Product>> getProductByCategory(String categoryName) {
        try{
            return new ResponseEntity<>(inventoryDao.findByCategory(categoryName), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Couldn't retrieve products that belong to category "+categoryName);
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }


    // Deduct from the stock of a product after purchasing
    public ResponseEntity<Integer> deductFromStock(Integer id, Integer quantity){
        try{
            Optional<Product> product = inventoryDao.findById(id);
            if(product.isPresent()){
                Product productFound = product.get();
                if(productFound.getQuantity()>=quantity)
                {
                    int newQuantity = productFound.getQuantity()-quantity;
                    productFound.setQuantity(newQuantity);
                    inventoryDao.save(productFound);
                    return new ResponseEntity<>(1,HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(0,HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Couldn't deduct from product #"+id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
    }

}
