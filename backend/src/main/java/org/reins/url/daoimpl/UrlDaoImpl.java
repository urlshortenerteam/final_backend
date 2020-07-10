package org.reins.url.daoimpl;
import org.reins.url.dao.UrlDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import org.reins.url.repository.Shorten_logRepository;
import org.reins.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
@Transactional
@Repository
@Service
public class UrlDaoImpl implements UrlDao {
    @Autowired
    private Shorten_logRepository shorten_logRepository;
    @Autowired
    private ShortenerRepository shortenerRepository;
    @Override
    public void addShortenLog(long creator_id,List<String> shortUrls,List<String> longUrls) {
        Shorten_log shorten_log=shorten_logRepository.save(new Shorten_log(creator_id,new Date()));
        long shorten_id=shorten_log.getId();
        for (int i=0;i<shortUrls.size();++i) shortenerRepository.insert(new Shortener(shorten_id,shortUrls.get(i),longUrls.get(i)));
    }
    @Override
    public List<Shorten_log> getShortenLog() {
        List<Shorten_log> shorten_logList=shorten_logRepository.findAll();
        for (int i=0;i<shorten_logList.size();i++) {
            long shorten_id=shorten_logList.get(i).getId();
            shorten_logList.get(i).setShortener(shortenerRepository.findByShorten_id(shorten_id));
        }
        return shorten_logList;
    }
}
