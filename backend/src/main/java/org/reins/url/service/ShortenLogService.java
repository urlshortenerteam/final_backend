package org.reins.url.service;

import org.reins.url.entity.ShortenLog;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ShortenLogService {
    CompletableFuture<String> addOneShortenLog(long creatorId, List<String> longUrls);

    CompletableFuture<List<String>> addShortenLog(long creatorId, List<String> longUrls);

    void changeShortenLog(ShortenLog shortenLog);

    CompletableFuture<ShortenLog> findById(long id);

    CompletableFuture<ShortenLog> findByShortUrl(String shortUrl);

    CompletableFuture<List<ShortenLog>> findTopTenOrderByVisitCount();
}
