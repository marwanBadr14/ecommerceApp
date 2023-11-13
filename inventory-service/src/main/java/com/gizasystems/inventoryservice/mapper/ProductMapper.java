package com.gizasystems.inventoryservice.mapper;


import com.gizasystems.inventoryservice.dto.ProductDto;
import com.gizasystems.inventoryservice.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {

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

    public List<ProductDto> transferToDto(List<Product> products){
        if (products == null)
            return null;

        List<ProductDto> productDtos = new ArrayList<>();
        ProductDto productDto;
        for (Product product:products){
            productDto = ProductDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .categoryId(product.getCategoryId())
                    .quantity(product.getQuantity())
                    .price(product.getPrice())
                    .imageUrl(product.getImageUrl())
                    .build();

            productDtos.add(productDto);
        }

        return productDtos;
    }

}
