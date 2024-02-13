package org.hstack.vmeta.extraction.category;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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

    private List<Category> category;

    @Getter
    @Builder
    public static class Category {
        Long id;
        String categoryType;
        float perc;
    }
}