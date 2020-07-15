package org.reins.url.serviceimpl;

import org.reins.url.dao.ShortenerDao;
import org.reins.url.entity.Shortener;
import org.reins.url.service.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortenerServiceImpl implements ShortenerService {
    @Autowired
    private ShortenerDao shortenerDao;

    @Override
    public void changeLong_url(Shortener shortener) {
        shortenerDao.changeLong_url(shortener);
    }

    @Override
    public List<Shortener> findByShorten_id(long shorten_id) {
        return shortenerDao.findByShorten_id(shorten_id);
    }

    @Override
    public List<Shortener> findByShort_url(String short_url) {
        return shortenerDao.findByShort_url(short_url);
    }
}
