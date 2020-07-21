package org.reins.url.dao;

import org.reins.url.entity.Shortener;

public interface ShortenerDao {
  void addShortener(long editorId, long shortenId, String longUrl);

  void changeShortener(Shortener shortener);
}
