package org.reins.url.dao;

import org.reins.url.entity.ShortenLog;

import java.util.List;

public interface ShortenLogDao {
  String addOneShortenLog(long creatorId, List<String> longUrls);

  List<String> addShortenLog(long creatorId, List<String> longUrls);

  void changeShortenLog(ShortenLog shortenLog);

  long count();

  List<ShortenLog> findAll();

  List<ShortenLog> findByCreatorId(long creatorId);

  ShortenLog findById(long id);

  ShortenLog findByShortUrl(String shortUrl);

  ShortenLog findTopOneOrderByVisitCount();

  List<ShortenLog> findTopTenOrderByVisitCount();

  long visitSum();
}
