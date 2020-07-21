package org.reins.url.dao;

import org.reins.url.entity.ShortenLog;

import java.util.List;

public interface ShortenLogDao {
  void addShortenLog(long creatorId, List<String> shortUrls, List<String> longUrls);

  void changeShortenLog(ShortenLog shortenLog);

  long count();

  List<ShortenLog> findAll();

  List<ShortenLog> findByCreatorId(long creatorId);

  ShortenLog findByShortUrl(String shortUrl);

  long visitSum();
}
