package org.hstack.vmeta.videoMetadata.metadata.indexScript;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Table(name="index_script")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexScript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Time time;
    private String content;
    private boolean expose;
    private boolean autocreated;
}
