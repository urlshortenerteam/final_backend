package org.reins.url.serviceimpl;

import org.reins.url.dao.Visit_logDao;
import org.reins.url.service.Visit_logService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Visit_logServiceImpl implements Visit_logService {
    @Autowired
    Visit_logDao visit_logDao;

    @Override
    public void addVisit_log(String shortener_id, String ip, Boolean device) {
        visit_logDao.addVisit_log(shortener_id, ip, device);
    }
}
