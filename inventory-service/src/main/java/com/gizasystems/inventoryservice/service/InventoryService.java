package com.gizasystems.inventoryservice.service;

import com.gizasystems.inventoryservice.dao.InventoryDao;
import com.gizasystems.inventoryservice.exception.ApiRequestException;
import com.gizasystems.inventoryservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    InventoryDao inventoryDao;

    // Retrieve all products in the inventory
    public List<Product> getAllProducts() {
        try{
            return inventoryDao.findAll();
        }catch (Exception e){
            throw new ApiRequestException("Couldn't retrieve all products");
        }
    }


    // Retrieve a list of products that belong to the same category
    public List<Product> getProductByCategory(String categoryName) {
        try{
            return inventoryDao.findByCategory(categoryName);
        }catch (Exception e){
            throw new ApiRequestException("Couldn't retrieve products that belong to category "+categoryName);
        }
    }


    // Deduct from the stock of a product after purchasing
    public void deductFromStock(Integer id, Integer quantity){
        try{
            Optional<Product> product = inventoryDao.findById(id);
            if(product.isPresent()){
                Product productFound = product.get();
                if(productFound.getQuantity()>=quantity)
                {
                    int newQuantity = productFound.getQuantity()-quantity;
                    productFound.setQuantity(newQuantity);
                    inventoryDao.save(productFound);
                }else
                    throw new ApiRequestException("Quantity requested from product #"+id+" is more than the quantity" +
                            "present in the stock");
            }
        }catch (Exception e){
            throw new ApiRequestException("Couldn't deduct from product #"+id);
        }
    }

}
