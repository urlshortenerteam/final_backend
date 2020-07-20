package org.reins.url.service;

import org.reins.url.entity.Shortener;

public interface ShortenerService {
    void addShortener(long editorId, long shortenId, String longUrl);

    void changeShortener(Shortener shortener);
}
