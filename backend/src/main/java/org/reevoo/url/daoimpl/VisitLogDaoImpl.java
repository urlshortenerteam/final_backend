package org.reevoo.url.daoimpl;

import org.reevoo.url.dao.VisitLogDao;
import org.reevoo.url.entity.VisitLog;
import org.reevoo.url.repository.VisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    public List<VisitLog> findByShortenerId(String shortenerId) {
        return visitLogRepository.findByShortenerId(shortenerId);
    }

    @Override
    public List<VisitLog> findOrderByVisitTime(Pageable pageable) {
        return visitLogRepository.findOrderByVisitTimeDesc(pageable);
    }
    @Override
    public List<VisitLog> findTop5ByShortenerIdOrderByVisitTimeDesc(List<String> shortenerId){
        return visitLogRepository.findTop5ByShortenerIdInOrderByVisitTimeDesc(shortenerId);
    }
}
