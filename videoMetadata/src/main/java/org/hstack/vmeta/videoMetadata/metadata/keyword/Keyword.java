package org.hstack.vmeta.videoMetadata.metadata.keyword;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name="keyword")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String keyword;

    @Column
    private float perc;

    @Column
    private boolean expose;

    @Column
    private boolean autocreated;

    @Column(name="metadata_id")
    private Long mid;
}
