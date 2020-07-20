package org.reins.url.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import org.reins.url.dao.ShortenLogDao;
import org.reins.url.dao.UsersDao;
import org.reins.url.dao.VisitLogDao;
import org.reins.url.entity.*;
import org.reins.url.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatServiceImpl implements StatService {
    @Autowired
    private ShortenLogDao shortenLogDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private VisitLogDao visitLogDao;

    @Override
    public List<Statistics> getStat(long id) {
        List<Statistics> res = new ArrayList<>();
        List<ShortenLog> shortenLogs = shortenLogDao.findByCreatorId(id);
        for (ShortenLog s : shortenLogs) {
            Statistics statistics = new Statistics();
            if (s.getShortener().size() == 0) continue;
            statistics.shortUrl = s.getShortUrl();
            statistics.count = s.getVisitCount();
            for (Shortener shortener : s.getShortener()) {
                List<VisitLog> visitLogs = visitLogDao.findByShortenerId(shortener.getId());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", shortener.getLongUrl());
                statistics.longUrl.add(jsonObject);
                for (VisitLog v : visitLogs) {
                    try {
                        statistics.addAreaDistr(v.getIp());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    statistics.addTimeDistr(v.getVisitTime());
                    statistics.addSourceDistr(v.getIp());
                }
            }
            res.add(statistics);
        }
        return res;
    }

    @Override
    public Statistics getShortStat(String shortUrl) {
        Statistics statistics = new Statistics();
        statistics.shortUrl = shortUrl;
        ShortenLog shortenLog = shortenLogDao.findByShortUrl(shortUrl);
        if (shortenLog == null) {
            statistics.count = -1;
            return statistics;
        }
        List<Shortener> shorteners = shortenLog.getShortener();
        for (Shortener shortener : shorteners) {
            List<VisitLog> visitLogs = visitLogDao.findByShortenerId(shortener.getId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", shortener.getLongUrl());
            statistics.longUrl.add(jsonObject);
            for (VisitLog v : visitLogs) {
                try {
                    statistics.addAreaDistr(v.getIp());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                statistics.addTimeDistr(v.getVisitTime());
                statistics.addSourceDistr(v.getIp());
            }
        }
        return statistics;
    }

    @Override
    public List<Users> getUserStat() {
        return usersDao.findAllUserStat();
    }

    @Override
    public List<Statistics> getAllUrls() {
        List<Statistics> res = new ArrayList<>();
        List<ShortenLog> shortenLogs = shortenLogDao.findAll();
        for (ShortenLog s : shortenLogs) {
            Statistics statistics = new Statistics();
            statistics.shortUrl = s.getShortUrl();
            statistics.creatorName = usersDao.findById(s.getCreatorId()).getName();
            statistics.createTime = s.getCreateTime();
            statistics.count = s.getVisitCount();
            for (Shortener shortener : s.getShortener()) {
                List<VisitLog> visitLogs = visitLogDao.findByShortenerId(shortener.getId());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", shortener.getLongUrl());
                statistics.longUrl.add(jsonObject);
                for (VisitLog v : visitLogs) {
                    try {
                        statistics.addAreaDistr(v.getIp());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    statistics.addTimeDistr(v.getVisitTime());
                    statistics.addSourceDistr(v.getIp());
                }
            }
            res.add(statistics);
        }
        return res;
    }

    @Override
    public JSONObject getNumberCount() {
        JSONObject res = new JSONObject();
        res.put("userCount", usersDao.count());
        res.put("shortUrlCount", shortenLogDao.count());
        res.put("visitCountTotal", shortenLogDao.visitSum());
        return res;
    }
}
