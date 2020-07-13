package org.reins.url.daoimpl;
import org.reins.url.dao.Visit_logDao;
import org.reins.url.entity.Visit_log;
import org.reins.url.repository.Visit_logRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
@Transactional
@Repository
@Service
public class Visit_logDaoImpl implements Visit_logDao {
    @Autowired
    Visit_logRepository visit_logRepository;
    @Override
    public void addVisit_log(String shortener_id,String ip,Boolean device) {
        Visit_log visit_log=new Visit_log();
        visit_log.setShortener_id(shortener_id);
        visit_log.setVisit_time(new Date());
        visit_log.setIp(ip);
        visit_log.setDevice(device);
        visit_logRepository.save(visit_log);
    }
    @Override
    public List<Visit_log> findByShortenerId(String shortener_id) {
        return visit_logRepository.findByShortener_id(shortener_id);
    }
}
