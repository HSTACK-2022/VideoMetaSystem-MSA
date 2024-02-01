package org.hstack.vmeta.extraction.indexScript;

import lombok.*;

import java.sql.Time;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexScriptDTO {

    private List<IndexScript> indexScript;

    @Getter
    @Builder
    static class IndexScript {
        Long id;
        Time time;
        String content;
        boolean expose;
        boolean autocreated;
    }
}
