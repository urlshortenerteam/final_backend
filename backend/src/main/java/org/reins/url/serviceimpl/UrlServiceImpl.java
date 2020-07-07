package org.reins.url.serviceimpl;
import org.reins.url.dao.UrlDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UrlServiceImpl implements UrlService {
    @Autowired
    UrlDao urlDao;
    @Override
    public void addLog(long creator_id,List<String> shortUrls,List<String> longUrls) {
        urlDao.addLog(creator_id,shortUrls,longUrls);
    }
    @Override
    public List<Shorten_log> getLog() {
        return urlDao.getLog();
    }
}
