package org.reins.url.serviceimpl;

import org.reins.url.dao.ShortenLogDao;
import org.reins.url.entity.ShortenLog;
import org.reins.url.service.ShortenLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ShortenLogServiceImpl implements ShortenLogService {
    @Autowired
    private ShortenLogDao shortenLogDao;

    @Override
    @Async
    public CompletableFuture<String> addOneShortenLog(long creatorId, List<String> longUrls) {
        return CompletableFuture.completedFuture(shortenLogDao.addOneShortenLog(creatorId, longUrls));
    }

    @Override
    @Async
    public CompletableFuture<List<String>> addShortenLog(long creatorId, List<String> longUrls) {
        return CompletableFuture.completedFuture(shortenLogDao.addShortenLog(creatorId, longUrls));
    }

    @Override
    @Async
    public CompletableFuture<ShortenLog> findById(long id) {
        return CompletableFuture.completedFuture(shortenLogDao.findById(id));
    }

    @Override
    @Async
    public CompletableFuture<ShortenLog> findByShortUrl(String shortUrl) {
        return CompletableFuture.completedFuture(shortenLogDao.findByShortUrl(shortUrl));
    }

    @Override
    @Async
    public CompletableFuture<List<ShortenLog>> findTopTenOrderByVisitCount() {
        return CompletableFuture.completedFuture(shortenLogDao.findTopTenOrderByVisitCount());
    }
}
