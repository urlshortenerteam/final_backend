package org.reins.url.serviceimpl;

import org.reins.url.dao.StatDao;
import org.reins.url.dao.UrlDao;
import org.reins.url.dao.UserDao;
import org.reins.url.dao.VisitDao;
import org.reins.url.entity.*;
import org.reins.url.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatServiceImpl implements StatService {
    @Autowired
    private StatDao statDao;
    @Autowired
    private VisitDao visitDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UrlDao urlDao;

    @Override
    public List<Statistics> getStat() {
        List<Statistics> res = new ArrayList<>();
        List<Shorten_log> shorten_logs = statDao.findAll();
        for (Shorten_log s : shorten_logs) {
            Statistics statistics = new Statistics();
            if (s.getShortener().size() == 0) continue;
            statistics.shortUrl = s.getShortener().get(0).getShort_url();
            for (Shortener shortener : s.getShortener()) {
                List<Visit_log> visit_logs = visitDao.findByShortenerId(shortener.getId());
                statistics.count += visit_logs.size();
                for (Visit_log v : visit_logs) {
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
        Statistics statistics = new Statistics();
        statistics.shortUrl = short_url;
        List<Shortener> shorteners = urlDao.findShortenerByShort_url(short_url);
        if (shorteners.size() == 0) return statistics;
        long shorten_id = shorteners.get(0).getShorten_id();
        Optional<Shorten_log> shorten_log = statDao.findById(shorten_id);
        if (!shorten_log.isPresent()) {
            statistics.count = -1;
            return statistics;
        }
        for (Shortener shortener : shorteners) {
            List<Visit_log> visit_logs = visitDao.findByShortenerId(shortener.getId());
            statistics.count += visit_logs.size();
            for (Visit_log v : visit_logs) {
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

    @Override
    public List<Users> getUserStat() {
        return userDao.findAllUserStat();
    }
}
