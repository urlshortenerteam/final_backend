package org.reins.url.daoimpl;
import org.reins.url.dao.VisitDao;
import org.reins.url.entity.Visit_log;
import org.reins.url.repository.Visit_logRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Transactional
@Repository
@Service
public class VisitDaoImpl implements VisitDao {
    @Autowired
    Visit_logRepository visit_logRepository;
    @Override
    public List<Visit_log> findByShortenerId(long shortener_id) {
        return visit_logRepository.findByShortener_id(shortener_id);
    }
}
