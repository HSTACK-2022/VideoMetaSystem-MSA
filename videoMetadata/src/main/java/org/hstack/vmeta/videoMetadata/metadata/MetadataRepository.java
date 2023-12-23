package org.hstack.vmeta.videoMetadata.metadata;

import org.hstack.vmeta.videoMetadata.metadata.category.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MetadataRepository extends JpaRepository<Metadata, Long> {

    Metadata save(Metadata metadata);

    Optional<Metadata> findById(Long id);

    Set<MetadataMapping> findByTitleContains(String title);

    Set<MetadataMapping> findByUploaderNameContains(String uploaderName);

    @Query("select m from Metadata m join fetch m.keyword k where k.keyword like %:keyword%")
    Set<MetadataMapping> findByKeywordContains(@Param("keyword") String keyword);

    @Query("select m from Metadata m join fetch m.category c where c.categoryType = :category")
    Set<MetadataMapping> findByCategory(@Param("category") CategoryType category);

    void deleteById(Long id);

    void deleteAll();
}