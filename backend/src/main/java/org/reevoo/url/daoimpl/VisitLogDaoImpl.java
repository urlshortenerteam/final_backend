package org.reevoo.url.daoimpl;

import org.reevoo.url.dao.VisitLogDao;
import org.reevoo.url.entity.VisitLog;
import org.reevoo.url.repository.VisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
