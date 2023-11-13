package com.gizasystems.inventoryservice.service;

import com.gizasystems.inventoryservice.dao.CategoryDao;
import com.gizasystems.inventoryservice.dao.ProductDao;
import com.gizasystems.inventoryservice.dto.ProductDto;
import com.gizasystems.inventoryservice.exception.CategoryNotFoundException;
import com.gizasystems.inventoryservice.exception.ProductNotFoundException;
import com.gizasystems.inventoryservice.mapper.ProductMapper;
import com.gizasystems.inventoryservice.model.Category;
import com.gizasystems.inventoryservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ProductMapper productMapper;

    // Retrieve a specific product by its id
    public ProductDto getProductById(Integer id) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("No product with id #" + id);

        return productMapper.transferToDto(productDao.findById(id).orElse(null));
    }

    // Retrieve the price of a specific item
    public BigDecimal getProductPrice(Integer id) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("Couldn't find product #"+id);

        return product.get().getPrice();
    }

    public String getProductName(Integer id) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("Couldn't find product #"+id);

        return product.get().getName();
    }

    // Add a new product
    public ProductDto addProduct(ProductDto productDto) {
        Optional<Category> category = categoryDao.findById(productDto.getCategoryId());
        if (category.isEmpty())
            throw new CategoryNotFoundException("Couldn't find a category with id #"+productDto.getCategoryId());


        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategoryId(productDto.getCategoryId());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setImageUrl(productDto.getImageUrl());
        productDao.save(product);

        Category categoryFound = category.get();
        List<Integer>productsId = categoryFound.getProductsId();
        productsId.add(product.getId());
        categoryFound.setProductsId(productsId);
        categoryDao.save(categoryFound);

        return productMapper.transferToDto(product);
    }

    // Edit an already existing product
    public ProductDto editProductById(Integer id, ProductDto productDto) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("Couldn't find a product with id #"+id);

        Product editedProduct = product.get();
        if (productDto.getName()!=null)
          editedProduct.setName(productDto.getName());
        if (productDto.getDescription()!=null)
         editedProduct.setDescription(productDto.getDescription());

        if(productDto.getCategoryId()!=null) {
            Optional<Category> category = categoryDao.findById(productDto.getCategoryId());
            if(category.isEmpty())
                throw new CategoryNotFoundException("Couldn't find a category with id #"+productDto.getCategoryId());


            Category newCategory = category.get();
            newCategory.getProductsId().add(editedProduct.getId());
            categoryDao.save(newCategory);

            Category oldCategory = categoryDao.findById(editedProduct.getCategoryId()).orElse(null);
            assert oldCategory != null;

            oldCategory.getProductsId().remove(editedProduct.getId());
            categoryDao.save(oldCategory);

            editedProduct.setCategoryId(productDto.getCategoryId());
        }

        if(productDto.getPrice()!=null)
            editedProduct.setPrice(productDto.getPrice());
        if(productDto.getQuantity()!=null)
            editedProduct.setQuantity(productDto.getQuantity());
        if(productDto.getImageUrl()!=null)
            editedProduct.setImageUrl(productDto.getImageUrl());

        productDao.save(editedProduct);
        return productMapper.transferToDto(editedProduct);
    }

    // Delete products by id
    public void deleteProductById(Integer id) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("Couldn't find a product with id:"+id);

        productDao.deleteById(id);
    }
}
