package org.reins.url.dao;

import org.reins.url.entity.ShortenLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShortenLogDao {
    String addOneShortenLog(long creatorId, List<String> longUrls);

    List<String> addShortenLog(long creatorId, List<String> longUrls);

    long count();

    List<ShortenLog> findAll();

    List<ShortenLog> findByCreatorId(long creatorId);

    Page<ShortenLog> findByCreatorIdPageable(long creatorId, Pageable pageable);

    ShortenLog findById(long id);

    ShortenLog findByShortUrl(String shortUrl);

    Page<ShortenLog> findPage(Pageable pageable);

    ShortenLog findTopOneOrderByVisitCount();

    List<ShortenLog> findTopTenOrderByVisitCount();

    long visitSum();
}
