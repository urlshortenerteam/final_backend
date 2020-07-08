package org.reins.url.serviceimpl;
import org.reins.url.dao.StatDao;
import org.reins.url.dao.VisitDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import org.reins.url.entity.Statistics;
import org.reins.url.entity.Visit_log;
import org.reins.url.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class StatServiceImpl implements StatService {
    @Autowired
    StatDao statDao;

    @Autowired
    VisitDao visitDao;

    @Override
    public List<Statistics> getStat() {
        List<Statistics> res=new ArrayList<>();
        List<Shorten_log> shorten_logs=statDao.findAll();
        for (Shorten_log s:shorten_logs) {
            Statistics statistics=new Statistics();
            statistics.shortUrl=s.getShortener().get(0).getShort_url();
            for (Shortener shortener:s.getShortener()) {
                long shortener_id=Long.parseLong(shortener.getId());
                List<Visit_log> visit_logs=visitDao.findByShortenerId(shortener_id);
                statistics.visit_count+=visit_logs.size();
                for (Visit_log v:visit_logs){
                    try {
                        statistics.addArea_distr(v.getIp());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    statistics.addTime_distr(v.getVisit_time());
                    statistics.addSource_distr(v.getIp());
                }
            }
            res.add(statistics);
        }
        return res;
    }

    @Override
    public Statistics getShortStat(String short_url) {
        Statistics statistics=new Statistics();
        statistics.shortUrl=short_url;
        List<Shortener> shorteners=statDao.findShortenerByShortUrl(short_url);
        long shorten_id=shorteners.get(0).getShorten_id();
        Optional<Shorten_log> shorten_log=statDao.findById(shorten_id);
        if (!shorten_log.isPresent()) {
            statistics.visit_count=-1;
            return statistics;
        }
        for (Shortener shortener:shorteners) {
            long shortener_id=Long.parseLong(shortener.getId());
            List<Visit_log> visit_logs=visitDao.findByShortenerId(shortener_id);
            statistics.visit_count+=visit_logs.size();
            for (Visit_log v:visit_logs){
                try {
                    statistics.addArea_distr(v.getIp());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                statistics.addTime_distr(v.getVisit_time());
                statistics.addSource_distr(v.getIp());
            }
        }
        return statistics;
    }
}
