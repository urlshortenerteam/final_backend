package org.reins.url.daoimpl;

import org.reins.url.dao.StatDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import org.reins.url.repository.Shorten_logRepository;
import org.reins.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
@Service
public class StatDaoImpl implements StatDao {
    @Autowired
    private Shorten_logRepository shorten_logRepository;
    @Autowired
    private ShortenerRepository shortenerRepository;

    @Override
    public List<Shorten_log> findAll() {
        List<Shorten_log> list = shorten_logRepository.findAll();
        for (Shorten_log s : list) {
            s.setShortener(shortenerRepository.findByShorten_id(s.getId()));
        }
        return list;
    }

    @Override
    public List<Shorten_log> findByCreator_id(long creator_id) {
        List<Shorten_log> list = shorten_logRepository.findByCreator_id(creator_id);
        for (Shorten_log s : list) {
            s.setShortener(shortenerRepository.findByShorten_id(s.getId()));
        }
        return list;
    }

    @Override
    public Optional<Shorten_log> findById(long shorten_id) {
        return shorten_logRepository.findById(shorten_id);
    }
}
