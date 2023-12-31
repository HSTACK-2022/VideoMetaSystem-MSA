package org.hstack.vmeta.videoMetadata.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Video save(Video video);

    Optional<Video> findById(Long id);

    Optional<Video> findByTitleContains(String title);

    void deleteById(Long id);

    void deleteAll();

}
