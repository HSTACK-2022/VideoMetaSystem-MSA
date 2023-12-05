package org.hstack.vmeta.videoMetadata.metadata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MetadataRepository extends JpaRepository<Metadata, Long> {

    Metadata save(Metadata video);

    Optional<Metadata> findById(Long id);

//    List<Metadata> findByTitleContains(String title);
//
//    List<Metadata> findByKeywordContains(String keyword);
//
//    List<Metadata> findByCategoryContains(String category);
//
//    List<Metadata> findByUploaderNameContains(String uploaderName);

    void deleteById(Long id);

    void deleteAll();
}