package org.reins.url.repository;

import org.reins.url.entity.ShortenLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShortenLogRepository extends JpaRepository<ShortenLog, Long> {
    @Query("select s from ShortenLog s order by s.visitCount desc")
    List<ShortenLog> findAllOrderByVisitCount();

    @Query("select s from ShortenLog s where s.creatorId=:creatorId")
    List<ShortenLog> findByCreatorId(@Param("creatorId") long creatorId);

    @Query("select s from ShortenLog s where s.shortUrl=:shortUrl")
    ShortenLog findByShortUrl(@Param("shortUrl") String shortUrl);

    @Query("select sum(s.visitCount) from ShortenLog s")
    long visitSum();
}
