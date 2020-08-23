package org.reevoo.url.service;

import org.reevoo.url.entity.ShortenLog;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ShortenLogService {
    CompletableFuture<String> addOneShortenLog(long creatorId, List<String> longUrls);

    CompletableFuture<List<String>> addShortenLog(long creatorId, List<String> longUrls);

    CompletableFuture<ShortenLog> findById(long id);

    CompletableFuture<ShortenLog> findByShortUrl(String shortUrl);

    CompletableFuture<List<ShortenLog>> findTopTenOrderByVisitCount();
}
