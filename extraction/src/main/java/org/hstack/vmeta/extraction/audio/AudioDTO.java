package org.hstack.vmeta.extraction.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioDTO {

    private List<Script> script;

    class Script {

        private Long id;
        private Time time;
        private String content;

    }
}