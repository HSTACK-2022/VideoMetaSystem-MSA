package org.hstack.vmeta.videoMetadata.metadata;

import org.hstack.vmeta.videoMetadata.metadata.category.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MetadataRepository extends JpaRepository<Metadata, Long> {

    Metadata save(Metadata video);

    Optional<Metadata> findById(Long id);

    List<MetadataMapping> findByTitleContains(String title);

    List<MetadataMapping> findByUploaderNameContains(String uploaderName);

    @Query("select m from Metadata m join fetch m.keyword k where k.keyword like %:keyword%")
    List<MetadataMapping> findByKeywordContains(@Param("keyword") String keyword);

    //@Query("select m from Metadata m join fetch m.category c where c.categoryType like %:category%")
    //List<MetadataMapping> findByCategoryContains(@Param("category") String category);


    void deleteById(Long id);

    void deleteAll();
}