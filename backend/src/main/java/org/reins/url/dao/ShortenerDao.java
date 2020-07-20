package org.reins.url.dao;

import org.reins.url.entity.Shortener;

import java.util.List;

public interface ShortenerDao {
  void addShortener(long shorten_id, String short_url, String long_url);

  void changeLong_url(Shortener shortener);

  void deleteShortener(String id);

  List<Shortener> findByShort_url(String short_url);
}
