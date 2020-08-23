package org.reevoo.url.dao;

import org.reevoo.url.entity.Shortener;

public interface ShortenerDao {
    void addShortener(long editorId, long shortenId, String longUrl);

    void changeShortener(Shortener shortener);

    Shortener findById(String id);
}
