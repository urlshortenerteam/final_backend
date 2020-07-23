package org.reins.url.repository;

import org.reins.url.entity.ShortenLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShortenLogRepository extends JpaRepository<ShortenLog, Long> {
    @Query("select s from ShortenLog s where s.creatorId=:creatorId")
    List<ShortenLog> findByCreatorId(@Param("creatorId") long creatorId);

    @Query("select s from ShortenLog s where s.shortUrl=:shortUrl")
    ShortenLog findByShortUrl(@Param("shortUrl") String shortUrl);

    @Query(value = "select s from ShortenLog s order by s.visit_count desc limit 1", nativeQuery = true)
    List<ShortenLog> findTopOneOrderByVisitCount();

    @Query(value = "select s from ShortenLog s order by s.visit_count desc limit 10", nativeQuery = true)
    List<ShortenLog> findTopTenOrderByVisitCount();

    @Query("select sum(s.visitCount) from ShortenLog s")
    long visitSum();
}
