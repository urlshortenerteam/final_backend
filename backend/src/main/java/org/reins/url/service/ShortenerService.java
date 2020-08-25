package org.reins.url.service;

import org.reins.url.entity.Shortener;

import java.util.concurrent.CompletableFuture;

public interface ShortenerService {
    void addShortener(long editorId, long shortenId, String longUrl);

    void changeShortener(Shortener shortener);

    CompletableFuture<Shortener> findById(String id);
}
