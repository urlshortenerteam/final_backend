package org.reins.url.controller;

import com.alibaba.fastjson.JSONObject;
import org.reins.url.entity.ShortenLog;
import org.reins.url.entity.Shortener;
import org.reins.url.entity.VisitLog;
import org.reins.url.service.ShortenLogService;
import org.reins.url.service.ShortenerService;
import org.reins.url.service.StatService;
import org.reins.url.service.VisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StatController {
    @Autowired
    private ShortenLogService shorten_logService;
    @Autowired
    private ShortenerService shortenerService;
    @Autowired
    private StatService statService;
    @Autowired
    private VisitLogService visit_logService;

    @CrossOrigin
    @RequestMapping("/getStat")
    public JSONObject getStat(@RequestParam("id") long id) {
        JSONObject res = new JSONObject();
        res.put("data", statService.getStat(id));
        return res;
    }

    @CrossOrigin
    @RequestMapping("/getShortStat")
    public JSONObject getShortStat(@RequestParam("id") long id, @RequestParam("short") String short_url) {
        JSONObject res = new JSONObject();
        res.put("data", statService.getShortStat(short_url));
        return res;
    }

    @CrossOrigin
    @RequestMapping("/getUserStat")
    public JSONObject getUserStat() {
        JSONObject res = new JSONObject();
        res.put("data", statService.getUserStat());
        return res;
    }

    @CrossOrigin
    @RequestMapping("/getReal")
    public JSONObject getReal(@RequestParam("id") long id) {
        List<VisitLog> visit_logList = visit_logService.findAllOrderByVisit_time();
        JSONObject res = new JSONObject();
        List<JSONObject> logs = new ArrayList<>();
        for (int i = 0; i < visit_logList.size() && logs.size() < 5; i++) {
            VisitLog visit_log = visit_logList.get(i);
            Shortener shortener = shortenerService.findById(visit_log.getShortenerId());
            if (shortener == null) continue;
            ShortenLog shorten_log = shorten_logService.findById(shortener.getShortenId());
            if (shorten_log != null && shorten_log.getCreatorId() == id) {
                JSONObject tmp = new JSONObject();
                tmp.put("shortUrl", shortener.getShort_url());
                tmp.put("long", shortener.getLongUrl());
                tmp.put("ip", visit_log.getIp());
                tmp.put("source", "Browser");
                tmp.put("time", visit_log.getVisitTime());
                logs.add(tmp);
            }
        }
        res.put("data", logs);
        return res;
    }
}
