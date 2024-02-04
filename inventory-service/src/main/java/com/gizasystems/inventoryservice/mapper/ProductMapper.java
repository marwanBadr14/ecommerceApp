package com.gizasystems.inventoryservice.mapper;


import com.gizasystems.inventoryservice.dto.ProductDto;
import com.gizasystems.inventoryservice.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    // From entity to dto
    public ProductDto transferToDto(Product product){
        if (product == null)
            return null;

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategoryId())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }

    // From list of entities to dtos
    public List<ProductDto> transferToDto(List<Product> products){
        return Optional.ofNullable(products)
                .map(list -> list.stream()
                        .map(product -> ProductDto.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .categoryId(product.getCategoryId())
                                .quantity(product.getQuantity())
                                .price(product.getPrice())
                                .imageUrl(product.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    // From dto to entity
    public Product transferToEntity(ProductDto productDto){
        if (productDto == null)
            return null;

        Product product = new Product();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setCategoryId(productDto.categoryId());
        product.setPrice(productDto.price());
        product.setQuantity(productDto.quantity());
        product.setImageUrl(productDto.imageUrl());

        return product;
    }


}
