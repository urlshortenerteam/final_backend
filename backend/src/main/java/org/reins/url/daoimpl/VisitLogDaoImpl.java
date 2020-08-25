package org.reins.url.daoimpl;

import org.reins.url.dao.VisitLogDao;
import org.reins.url.entity.VisitLog;
import org.reins.url.repository.VisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Repository
@Service
public class VisitLogDaoImpl implements VisitLogDao {
    @Autowired
    private VisitLogRepository visitLogRepository;

    @Override
    public List<VisitLog> findAllOrderByVisitTime() {
        return visitLogRepository.findAllOrderByVisitTime();
    }

    @Override
    public List<VisitLog> findByShortenerId(String shortenerId) {
        return visitLogRepository.findByShortenerId(shortenerId);
    }
}