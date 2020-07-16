package org.reins.url.service;

import org.reins.url.entity.Shortener;

import java.util.List;

public interface ShortenerService {
    void addShortener(long shorten_id, String short_url, String long_url);

    void changeLong_url(Shortener shortener);

    void deleteShortener(String id);

    List<Shortener> findByShorten_id(long shorten_id);

    List<Shortener> findByShort_url(String short_url);
}
