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
    ShortenerDao shortenerDao;
    @Override
    public void changeLong_url(Shortener shortener) {
        shortenerDao.changeLong_url(shortener);
    }
    @Override
    public List<Shortener> findShortenerByShort_url(String short_url) {
        return shortenerDao.findShortenerByShort_url(short_url);
    }
}
