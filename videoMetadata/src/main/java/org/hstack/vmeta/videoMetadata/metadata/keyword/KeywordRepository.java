package org.hstack.vmeta.videoMetadata.metadata.keyword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    // List<Keyword> saveAll(List<Keyword> keywordList);

    void deleteByMid(Long mid);
}
