package org.reins.url.serviceimpl;

import org.reins.url.dao.Shorten_logDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.service.Shorten_logService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Shorten_logServiceImpl implements Shorten_logService {
    @Autowired
    Shorten_logDao shorten_logDao;

    @Override
    public void addShorten_log(long creator_id, List<String> shortUrls, List<String> longUrls) {
        shorten_logDao.addShorten_log(creator_id, shortUrls, longUrls);
    }

    @Override
    public Shorten_log findById(long id) {
        return shorten_logDao.findById(id);
    }
}
