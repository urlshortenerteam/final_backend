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
import java.util.ArrayList;
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
    public void addLog(long creator_id,List<String> shortUrls,List<String> longUrls) {
        shorten_logRepository.addLog(creator_id,new Date());
        long shorten_id=0;
        List<Shorten_log> shorten_logList=shorten_logRepository.getLog();
        for (int i=0;i<shorten_logList.size();++i) {
            Shorten_log shorten_log=shorten_logList.get(i);
            if (shorten_log.getId()>shorten_id) shorten_id=shorten_log.getId();
        }
        for (int i=0;i<shortUrls.size();++i) shortenerRepository.insert(new Shortener(shorten_id,shortUrls.get(i),longUrls.get(i)));
    }
    @Override
    public List<Shorten_log> getLog() {
        List<Shorten_log> shorten_logList=shorten_logRepository.getLog();
        List<Shortener> shortenerList=shortenerRepository.findAll();
        for (int i=0;i<shorten_logList.size();i++) {
            long shorten_id=shorten_logList.get(i).getId();
            List<Shortener> tmp=new ArrayList<>();
            for (int j=0;j<shortenerList.size();j++) {
                Shortener shortener=shortenerList.get(j);
                if (shortener.getShorten_id()==shorten_id) tmp.add(shortener);
            }
            shorten_logList.get(i).setShortener(tmp);
        }
        return shorten_logList;
    }
}
