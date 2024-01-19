package org.hstack.vmeta.videoMetadata.metadata.script;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Table(name="script")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Time time;

    private String content;

    @Column(name="metadata_id")
    private Long mid;

}
