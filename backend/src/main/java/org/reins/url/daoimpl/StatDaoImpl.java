package org.reins.url.daoimpl;
import org.reins.url.dao.StatDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.repository.Shorten_logRepository;
import org.reins.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
public class StatDaoImpl implements StatDao {
    @Autowired
    Shorten_logRepository shorten_logRepository;
    @Autowired
    ShortenerRepository shortenerReposiy;
    public List<Shorten_log> findAll(){
        return shorten_logRepository.findAll();
    }
}
