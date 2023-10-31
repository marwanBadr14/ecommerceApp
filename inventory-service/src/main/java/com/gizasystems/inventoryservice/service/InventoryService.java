package com.gizasystems.inventoryservice.service;


import com.gizasystems.inventoryservice.dao.InventoryDao;
import com.gizasystems.inventoryservice.entity.Category;
import com.gizasystems.inventoryservice.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    // Retrieve all products of a specific company
    public ResponseEntity<List<Product>> getAllCompanyProducts(Integer companyId) {
        try{
            List<Product> companyProducts = inventoryDao.findByCompany(companyId);
            return new ResponseEntity<>(companyProducts, HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Couldn't retrieve products of company #"+companyId);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    // Retrieve a specific product by its id
    public ResponseEntity<Product> getProductById(Integer id) {
        try{
            return new ResponseEntity<>(inventoryDao.findById(id).get(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Couldn't retrieve product #"+id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    // Retrieve a specific product by its id
    public ResponseEntity<List<Product>> getProductByCategory(Category category) {
        try{
            return new ResponseEntity<>(inventoryDao.findByCategory(category.getName()), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Couldn't retrieve products that belong to category "+category.getName());
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    // Add a new product
    public ResponseEntity<Product> addProduct(Product product) {
        try{
            return new ResponseEntity<>(inventoryDao.save(product),HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println("Couldn't add product "+product.getName());
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    // Edit an already existing product
    public ResponseEntity<Product> editProductById(Integer id, Product product) {
        try {
            Optional<Product> prod = inventoryDao.findById(id);
            if(prod.isPresent()){
                Product editedProduct = prod.get();
                editedProduct.setId(id);
                editedProduct.setName(product.getName());
                editedProduct.setDescription(product.getDescription());
                return new ResponseEntity<>(inventoryDao.save(editedProduct),HttpStatus.OK);
            }
        }catch (Exception e){
            System.out.println("Couldn't edit product #"+id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    // Delete products by id
    public ResponseEntity<Product> deleteProductById(Integer id) {
        try{
            Optional<Product> product = inventoryDao.findById(id);
            if(product.isPresent()){
                inventoryDao.deleteById(id);
                return new ResponseEntity<>(null,HttpStatus.OK);
            }
        }catch (Exception e){
            System.out.println("Couldn't delete product #"+id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
