package com.gizasystems.inventoryservice.mapper;

import com.gizasystems.inventoryservice.dto.CategoryDto;
import com.gizasystems.inventoryservice.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {

    // From entity to dto
    public CategoryDto transferToDto(Category category){
        if(category == null)
            return null;

       return CategoryDto.builder()
               .id(category.getId())
               .name(category.getName())
               .build();
    }

    // From list of entities to a list of dtos
    public List<CategoryDto> transferToDto(List<Category> categories){
        if(categories == null)
            return null;

        List<CategoryDto> categoryDtos = new ArrayList<>();
        CategoryDto categoryDto;

        for (Category category: categories){
            categoryDto = CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();

            categoryDtos.add(categoryDto);
        }

        return categoryDtos;
    }

}
