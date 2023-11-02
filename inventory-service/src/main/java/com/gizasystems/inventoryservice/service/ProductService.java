package com.gizasystems.inventoryservice.service;

import com.gizasystems.inventoryservice.dao.CategoryDao;
import com.gizasystems.inventoryservice.dao.ProductDao;
import com.gizasystems.inventoryservice.entity.Category;
import com.gizasystems.inventoryservice.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    CategoryDao categoryDao;

    // Retrieve a specific product by its id
    public ResponseEntity<Product> getProductById(Integer id) {
        try{
            return new ResponseEntity<>(productDao.findById(id).get(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Couldn't retrieve product #"+id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    // Retrieve the price of a specific item
    public ResponseEntity<BigDecimal> getProductPrice(Integer id) {
        try{
            Optional<Product> product = productDao.findById(id);
            if(product.isPresent()){
                Product productFound = product.get();
                return new ResponseEntity<>(productFound.getPrice(), HttpStatus.OK);
            }

        }catch (Exception e){
            System.out.println("Couldn't find product #"+id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    // Add a new product
    public ResponseEntity<Product> addProduct(Product product) {
        try{
            Category category = categoryDao.findById(product.getCategoryId()).get();
            List<Integer>productsId = category.getProductsId();
            productDao.save(product);
            productsId.add(product.getId());
            category.setProductsId(productsId);
            categoryDao.save(category);
            return new ResponseEntity<>(product,HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println("Couldn't add product "+product.getName());
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    // Edit an already existing product
    public ResponseEntity<Product> editProductById(Integer id, Product product) {
        try {
            Optional<Product> prod = productDao.findById(id);
            if(prod.isPresent()){
                Product editedProduct = prod.get();
                if (product.getName()!=null)
                  editedProduct.setName(product.getName());
                if (product.getDescription()!=null)
                 editedProduct.setDescription(product.getDescription());
                if(product.getCategoryId()!=null)
                    editedProduct.setCategoryId(product.getCategoryId());
                if(product.getPrice()!=null)
                    editedProduct.setPrice(product.getPrice());
                if(product.getQuantity()!=null)
                    editedProduct.setQuantity(product.getQuantity());
                return new ResponseEntity<>(productDao.save(editedProduct),HttpStatus.OK);
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
            Optional<Product> product = productDao.findById(id);
            if(product.isPresent()){
                productDao.deleteById(id);
                return new ResponseEntity<>(null,HttpStatus.OK);
            }
        }catch (Exception e){
            System.out.println("Couldn't delete product #"+id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
