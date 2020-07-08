package org.reins.url.daoimpl;

import org.reins.url.dao.StatDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import org.reins.url.repository.Shorten_logRepository;
import org.reins.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class StatDaoImpl implements StatDao {
    @Autowired
    Shorten_logRepository shorten_logRepository;
    @Autowired
    ShortenerRepository shortenerRepository;
    public List<Shorten_log> findAll(){
        List<Shorten_log> list=shorten_logRepository.findAll();
        for (Shorten_log s:list){
            s.setShortener(shortenerRepository.findByShorten_id(s.getId()));
        }
        return list;
    }

    public List<Shorten_log> findByCreator_id(long creator_id){
        List<Shorten_log> list=shorten_logRepository.findByCreator_id(creator_id);
        for (Shorten_log s:list){
            s.setShortener(shortenerRepository.findByShorten_id(s.getId()));
        }
        return list;
    }

    public Optional<Shorten_log> findById(long shorten_id){
        return shorten_logRepository.findById(shorten_id);
    }

    public List<Shortener> findShortenerByShortUrl(String short_url){
        return shortenerRepository.findByShort_url(short_url);
    }
}
