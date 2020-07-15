package org.reins.url.dao;

import org.reins.url.entity.Shortener;

import java.util.List;

public interface ShortenerDao {
    void changeLong_url(Shortener shortener);

    List<Shortener> findByShorten_id(long shorten_id);

    List<Shortener> findByShort_url(String short_url);
}
