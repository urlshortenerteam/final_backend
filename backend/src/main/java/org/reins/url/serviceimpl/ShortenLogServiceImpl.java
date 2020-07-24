package org.reins.url.serviceimpl;

import org.reins.url.dao.ShortenLogDao;
import org.reins.url.entity.ShortenLog;
import org.reins.url.service.ShortenLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortenLogServiceImpl implements ShortenLogService {
  @Autowired
  private ShortenLogDao shortenLogDao;

  @Override
  public String addOneShortenLog(long creatorId, List<String> longUrls) {
    return shortenLogDao.addOneShortenLog(creatorId, longUrls);
  }

  @Override
  public List<String> addShortenLog(long creatorId, List<String> longUrls) {
    return shortenLogDao.addShortenLog(creatorId, longUrls);
  }

  @Override
  public void changeShortenLog(ShortenLog shortenLog) {
    shortenLogDao.changeShortenLog(shortenLog);
  }

  @Override
  public ShortenLog findById(long id) {
    return shortenLogDao.findById(id);
  }

  @Override
  public ShortenLog findByShortUrl(String shortUrl) {
    return shortenLogDao.findByShortUrl(shortUrl);
  }

  @Override
  public List<ShortenLog> findTopTenOrderByVisitCount() {
    return shortenLogDao.findTopTenOrderByVisitCount();
  }
}
