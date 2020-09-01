package org.reevoo.url.service;

import org.reevoo.url.entity.Shortener;

public interface ShortenerService {
    void addShortener(long editorId, long shortenId, String longUrl);

    void changeShortener(Shortener shortener);
}
