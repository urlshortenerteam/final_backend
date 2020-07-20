package org.reins.url.daoimpl;

import org.reins.url.dao.ShortenerDao;
import org.reins.url.entity.Shortener;
import org.reins.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@Service
public class ShortenerDaoImpl implements ShortenerDao {
    @Autowired
    private ShortenerRepository shortenerRepository;

    @Override
    public void addShortener(long shortenId, String longUrl) {
        Shortener shortener = new Shortener();
        shortener.setShortenId(shortenId);
        shortener.setLongUrl(longUrl);
        shortenerRepository.insert(shortener);
    }

    @Override
    public void changeShortener(Shortener shortener) {
        shortenerRepository.save(shortener);
    }

    @Override
    public Shortener findById(String id) {
        return shortenerRepository.findById(id).orElse(null);
    }
}
