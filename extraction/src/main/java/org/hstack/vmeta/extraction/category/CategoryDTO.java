package org.hstack.vmeta.extraction.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private List<CategoryDTO.Category> category;

    @Builder
    public static class Category {
        Long id;
        String categoryType;
        float perc;
    }
}