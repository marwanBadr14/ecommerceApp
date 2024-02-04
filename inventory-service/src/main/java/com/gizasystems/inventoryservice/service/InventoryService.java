package com.gizasystems.inventoryservice.service;

import com.gizasystems.inventoryservice.dao.InventoryDao;
import com.gizasystems.inventoryservice.dto.ProductDto;
import com.gizasystems.inventoryservice.exception.ProductNotFoundException;
import com.gizasystems.inventoryservice.exception.QuantityExceededException;
import com.gizasystems.inventoryservice.mapper.ProductMapper;
import com.gizasystems.inventoryservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    InventoryDao inventoryDao;
    ProductMapper productMapper;

    public InventoryService(InventoryDao inventoryDao, ProductMapper productMapper) {
        this.inventoryDao = inventoryDao;
        this.productMapper = productMapper;
    }

    // Retrieve all products in the inventory
    public List<ProductDto> getAllProducts() {
        List<Product> products = inventoryDao.findAll();
        if (products.isEmpty())
            throw new ProductNotFoundException("Couldn't find products");

        return productMapper.transferToDto(products);
    }


    // Retrieve a list of products that belong to the same category
    public List<ProductDto> getProductByCategory(String categoryName) {
        List<Product> products = inventoryDao.findByCategory(categoryName);
        if(products.isEmpty())
            throw new ProductNotFoundException("No products belong to category #"+categoryName);

        return productMapper.transferToDto(products);
    }


    // Deduct from the stock of a product after purchasing
    public Boolean deductFromStock(Integer id, Integer quantity){
        Optional<Product> product = inventoryDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("Couldn't find product with id #"+id);

        Product productFound = product.get();
        if(productFound.getQuantity()<quantity)
            throw new QuantityExceededException("Quantity requested from product #"+id+" is more than the quantity" +
                    "present in the stock");

        int newQuantity = productFound.getQuantity()-quantity;
        productFound.setQuantity(newQuantity);
        inventoryDao.save(productFound);
        return true;
    }

}
