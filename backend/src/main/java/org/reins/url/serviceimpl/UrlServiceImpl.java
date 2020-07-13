package org.reins.url.serviceimpl;
import org.reins.url.dao.UrlDao;
import org.reins.url.dao.VisitDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UrlServiceImpl implements UrlService {
    @Autowired
    UrlDao urlDao;
    @Autowired
    VisitDao visitDao;
    @Override
    public void addShortenLog(long creator_id,List<String> shortUrls,List<String> longUrls) {
        urlDao.addShortenLog(creator_id,shortUrls,longUrls);
    }
    @Override
    public void addVisitLog(String shortener_id,String ip,Boolean device) {
        visitDao.addVisitLog(shortener_id,ip,device);
    }
    @Override
    public List<Shorten_log> getShortenLog() {
        return urlDao.getShortenLog();
    }
}
