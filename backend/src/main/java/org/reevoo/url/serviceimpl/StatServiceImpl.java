package org.reevoo.url.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import org.reevoo.url.dao.ShortenLogDao;
import org.reevoo.url.entity.Statistics;
import org.reevoo.url.dao.UsersDao;
import org.reevoo.url.dao.VisitLogDao;
import org.reevoo.url.entity.ShortenLog;
import org.reevoo.url.entity.Shortener;
import org.reevoo.url.entity.Users;
import org.reevoo.url.entity.VisitLog;
import org.reevoo.url.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class StatServiceImpl implements StatService {
    @Autowired
    private ShortenLogDao shortenLogDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private VisitLogDao visitLogDao;

    @Override
    @Async
    public CompletableFuture<List<Statistics>> getAllUrls() {
        List<Statistics> res = new ArrayList<>();
        List<ShortenLog> shortenLogs = shortenLogDao.findAll();
        for (ShortenLog s : shortenLogs) {
            Statistics statistics = new Statistics();
            statistics.shortUrl = s.getShortUrl();
            statistics.creatorName = usersDao.findById(s.getCreatorId()).getName();
            statistics.createTime = s.getCreateTime();
            statistics.count = s.getVisitCount();
            for (Shortener shortener : s.getShortener()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", shortener.getLongUrl());
                statistics.longUrl.add(jsonObject);
            }
            res.add(statistics);
        }
        return CompletableFuture.completedFuture(res);
    }

    @Override
    @Async
    public CompletableFuture<JSONObject> getNumberCount() {
        JSONObject res = new JSONObject();
        res.put("userCount", usersDao.count());
        res.put("shortUrlCount", shortenLogDao.count());
        res.put("visitCountTotal", shortenLogDao.visitSum());
        ShortenLog shortenLog = shortenLogDao.findTopOneOrderByVisitCount();
        if (shortenLog != null)
            res.put("shortUrl", shortenLog.getShortUrl());
        return CompletableFuture.completedFuture(res);
    }

    @Override
    @Async
    public CompletableFuture<JSONObject> getPagedUrls(Pageable pageable) {
        Page<ShortenLog> shortenLogs = shortenLogDao.findPage(pageable);
        List<Statistics> res = new ArrayList<>();
        for (ShortenLog s : shortenLogs) {
            Statistics statistics = new Statistics();
            statistics.shortUrl = s.getShortUrl();
            statistics.creatorName = usersDao.findById(s.getCreatorId()).getName();
            statistics.createTime = s.getCreateTime();
            statistics.count = s.getVisitCount();
            for (Shortener shortener : s.getShortener()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", shortener.getLongUrl());
                statistics.longUrl.add(jsonObject);
            }
            res.add(statistics);
        }
        JSONObject ans = new JSONObject();
        ans.put("data", res);
        ans.put("totalElements", shortenLogs.getTotalElements());
        return CompletableFuture.completedFuture(ans);
    }

    @Override
    @Async
    public CompletableFuture<Statistics> getShortStat(String shortUrl) {
        Statistics statistics = new Statistics();
        statistics.shortUrl = shortUrl;
        ShortenLog shortenLog = shortenLogDao.findByShortUrl(shortUrl);
        if (shortenLog == null) {
            statistics.count = -1;
            return CompletableFuture.completedFuture(statistics);
        }
        statistics.count = shortenLog.getVisitCount();
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
                statistics.addSourceDistr(v.getDevice());
            }
        }
        return CompletableFuture.completedFuture(statistics);
    }

    @Override
    @Async
    public CompletableFuture<List<Statistics>> getStat(long id) {
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
                    statistics.addSourceDistr(v.getDevice());
                }
            }
            res.add(statistics);
        }
        return CompletableFuture.completedFuture(res);
    }

    @Override
    @Async
    public CompletableFuture<JSONObject> getStatPageable(long id, Pageable pageable) {
        List<Statistics> res = new ArrayList<>();
        Page<ShortenLog> shortenLogs = shortenLogDao.findByCreatorIdPageable(id, pageable);
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
                    statistics.addSourceDistr(v.getDevice());
                }
            }
            res.add(statistics);
        }
        JSONObject ans = new JSONObject();
        ans.put("data", res);
        ans.put("totalElements", shortenLogs.getTotalElements());
        return CompletableFuture.completedFuture(ans);
    }

    @Override
    @Async
    public CompletableFuture<List<String>> getUserShorteners(long id){
        List<Shortener> shorteners = shortenLogDao.getUserShorteners(id);
        List<String> ans=new ArrayList<>();
        for (Shortener s:shorteners){
            if (s.getLongUrl().equals("BANNED"))
                continue;
            ans.add(s.getId());
        }
        return CompletableFuture.completedFuture(ans);
    }

    @Override
    @Async
    public CompletableFuture<List<Users>> getUserStat() {
        return CompletableFuture.completedFuture(usersDao.findAllUserStat());
    }
}
