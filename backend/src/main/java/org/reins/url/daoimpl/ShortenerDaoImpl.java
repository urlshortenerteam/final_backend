package org.reins.url.daoimpl;

import org.reins.url.dao.ShortenerDao;
import org.reins.url.entity.Shortener;
import org.reins.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
@Service
public class ShortenerDaoImpl implements ShortenerDao {
    @Autowired
    private ShortenerRepository shortenerRepository;

    @Override
    public void changeLong_url(Shortener shortener) {
        shortenerRepository.save(shortener);
    }

    @Override
    public List<Shortener> findShortenerByShort_url(String short_url) {
        return shortenerRepository.findByShort_url(short_url);
    }
}
