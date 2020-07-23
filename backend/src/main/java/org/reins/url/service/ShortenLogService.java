package org.reins.url.service;

import org.reins.url.entity.ShortenLog;

import java.util.List;

public interface ShortenLogService {
    String addOneShortenLog(long creatorId, List<String> longUrls);

    List<String> addShortenLog(long creatorId, List<String> longUrls);

    void changeShortenLog(ShortenLog shortenLog);

    ShortenLog findById(long id);

    ShortenLog findByShortUrl(String shortUrl);

    List<ShortenLog> findTopTenOrderByVisitCount();
}
