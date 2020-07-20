package org.reins.url.dao;

import org.reins.url.entity.ShortenLog;

import java.util.List;
import java.util.Optional;

public interface ShortenLogDao {
  void addShortenLog(long creatorId, List<String> shortUrls, List<String> longUrls);

  ShortenLog findById(long id);

  List<ShortenLog> findByCreatorId(long creatorId);

  Optional<ShortenLog> findByShortUrl(String shortUrl);

  List<ShortenLog> findAll();

  long count();

  long visitSum();
}
