package org.reins.url.serviceimpl;

import org.reins.url.dao.Shorten_logDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.service.Shorten_logService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Shorten_logServiceImpl implements Shorten_logService {
  @Autowired
  private Shorten_logDao shortenLogDao;

  @Override
  public void addShorten_log(long creatorId, List<String> shortUrls, List<String> longUrls) {
    shortenLogDao.addShorten_log(creatorId, shortUrls, longUrls);
  }

  @Override
  public Shorten_log findById(long id) {
    return shortenLogDao.findById(id);
  }
}
