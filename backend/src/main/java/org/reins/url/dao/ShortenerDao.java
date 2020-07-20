package org.reins.url.dao;

import org.reins.url.entity.Shortener;

import java.util.List;

public interface ShortenerDao {
  void addShortener(long shortenId, String longUrl);

  void changeShortener(Shortener shortener);

  Shortener findById(String id);

  List<Shortener> findByShortenId(long shortenId);
}
