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
    public void addShortenLog(long creatorId, List<String> shortUrls, List<String> longUrls) {
        shortenLogDao.addShortenLog(creatorId, shortUrls, longUrls);
    }

    @Override
    public void changeShortenLog(ShortenLog shortenLog) {
        shortenLogDao.changeShortenLog(shortenLog);
    }

    @Override
    public ShortenLog findByShortUrl(String shortUrl) {
        return shortenLogDao.findByShortUrl(shortUrl);
    }
}
