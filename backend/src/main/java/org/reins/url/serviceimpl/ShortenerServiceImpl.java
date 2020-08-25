package org.reins.url.serviceimpl;

import org.reins.url.dao.ShortenerDao;
import org.reins.url.entity.Shortener;
import org.reins.url.service.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ShortenerServiceImpl implements ShortenerService {
    @Autowired
    private ShortenerDao shortenerDao;

    @Override
    @Async
    public void addShortener(long editorId, long shortenId, String longUrl) {
        shortenerDao.addShortener(editorId, shortenId, longUrl);
    }

    @Override
    @Async
    public void changeShortener(Shortener shortener) {
        shortenerDao.changeShortener(shortener);
    }

    @Override
    @Async
    public CompletableFuture<Shortener> findById(String id) {
        return CompletableFuture.completedFuture(shortenerDao.findById(id));
    }
}
