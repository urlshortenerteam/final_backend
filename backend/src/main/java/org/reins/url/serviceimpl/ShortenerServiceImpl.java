package org.reins.url.serviceimpl;

import org.reins.url.dao.ShortenerDao;
import org.reins.url.entity.Shortener;
import org.reins.url.service.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortenerServiceImpl implements ShortenerService {
  @Autowired
  private ShortenerDao shortenerDao;

  @Override
  public void addShortener(long editorId, long shortenId, String longUrl) {
    shortenerDao.addShortener(editorId, shortenId, longUrl);
  }

  @Override
  public void changeShortener(Shortener shortener) {
    shortenerDao.changeShortener(shortener);
  }

  @Override
  public Shortener findById(String id) {
    return shortenerDao.findById(id);
  }
}
