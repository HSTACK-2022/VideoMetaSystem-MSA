package org.hstack.vmeta.extraction.keyword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordDTO {

    private List<Keyword> keyword;

    @Getter
    @Builder
    public static class Keyword {
        Long id;
        String keyword;
        float perc;
        boolean expose;
        boolean autocreated;
    }
}