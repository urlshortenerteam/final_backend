package org.reins.url.repository;

import org.reins.url.entity.ShortenLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShortenLogRepository extends JpaRepository<ShortenLog, Long> {

    Page<ShortenLog> findAll(Pageable pageable);

    @Query("select s from ShortenLog s where s.creatorId=:creatorId")
    List<ShortenLog> findByCreatorId(@Param("creatorId") long creatorId);

    @Query(value = "select s from ShortenLog s where s.creatorId=:creatorId",
            countQuery = "select count(s) from ShortenLog s where s.creatorId=:creatorId")
    Page<ShortenLog> findByCreatorId(@Param("creatorId") long creatorId, Pageable pageable);

    @Query("select s from ShortenLog s where s.shortUrl=:shortUrl")
    ShortenLog findByShortUrl(@Param("shortUrl") String shortUrl);

    ShortenLog findTopByOrderByVisitCountDesc();

    List<ShortenLog> findTop10ByOrderByVisitCountDesc();

    @Query("select sum(s.visitCount) from ShortenLog s")
    Long visitSum();
}
