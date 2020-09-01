package org.reevoo.url.serviceimpl;

import org.reevoo.url.dao.ShortenerDao;
import org.reevoo.url.entity.Shortener;
import org.reevoo.url.service.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
}
