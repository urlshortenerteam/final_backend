package org.reevoo.url.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.reevoo.url.service.ShortenLogService;
import org.reevoo.url.service.ShortenerService;
import org.reevoo.url.service.VisitLogService;
import org.reevoo.url.util.JwtUtil;
import org.reevoo.url.entity.ShortenLog;
import org.reevoo.url.entity.Shortener;
import org.reevoo.url.entity.VisitLog;
import org.reevoo.url.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StatController {
    @Autowired
    private ShortenerService shortenerService;
    @Autowired
    private ShortenLogService shortenLogService;
    @Autowired
    private StatService statService;
    @Autowired
    private VisitLogService visitLogService;

    /**
     * handle the request "/getStat" and return the statistics of the user's Urls.
     *
     * @param jwt the jwt in requestHeader used for getting the user's id
     * @return {data:
     * [
     * {
     * shortUrl:String,
     * longUrl:JSONArray
     * count:Integer,
     * areaDistr:JSONArray,
     * timeDistr:JSONArray,
     * sourceDistr:JSONArray
     * },
     * {……},
     * ……
     * ]
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getStat")
    public JSONObject getStat(@RequestHeader("Authorization") String jwt) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        JSONObject res = new JSONObject();
        res.put("data", statService.getStat(Long.parseLong(c.get("id").toString())).get());
        return res;
    }

    /**
     * handle the request "/getStatPageable" and return the statistics of pageable Urls.
     * It's similar to "/getStat"
     *
     * @param jwt the jwt in requestHeader used for checking the user's type
     * @return {data:{
     * data:[
     * {
     * shortUrl:String,
     * longUrl:JSONArray
     * count:Integer,
     * areaDistr:JSONArray,
     * timeDistr:JSONArray,
     * sourceDistr:JSONArray
     * },
     * {……},
     * ……
     * ],
     * totalElements:long
     * }
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getStatPageable")
    public JSONObject getStatPageable(@RequestHeader("Authorization") String jwt, @RequestParam("pageCount") int pageCount, @RequestParam("pageSize") int pageSize) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        JSONObject res = new JSONObject();
        Pageable pageable = PageRequest.of(pageCount, pageSize);
        res.put("data", statService.getStatPageable(Long.parseLong(c.get("id").toString()), pageable).get());
        return res;
    }

    /**
     * handle the request "/getShortStat" and return the statistics of a single Url.
     *
     * @param jwt       the jwt in requestHeader used for getting the user's id
     * @param short_url the url whose statistics is required
     * @return {data:{
     * shortUrl:String,
     * longUrl:JSONArray
     * count:Integer,
     * area_distr:JSONArray,
     * time_distr:JSONArray,
     * source_distr:JSONArray
     * }
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getShortStat")
    public JSONObject getShortStat(@RequestHeader("Authorization") String jwt, @RequestParam("short") String short_url) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        JSONObject res = new JSONObject();
        res.put("data", statService.getShortStat(short_url).get());
        return res;
    }

    /**
     * handle the request "/getUserStat" and return the information of all users.
     * It can only be requested by administrators.
     *
     * @param jwt the jwt in requestHeader used for checking the user's type
     * @return {data:[
     * {
     * id:Long,
     * name:String,
     * role:Integer,
     * visit_count:Long,
     * },
     * {……},
     * ……
     * ],
     * not_administrator: Boolean
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getUserStat")
    public JSONObject getUserStat(@RequestHeader("Authorization") String jwt) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        if ((int) c.get("role") != 0) {
            JSONObject res = new JSONObject();
            res.put("not_administrator", true);
            return res;
        }
        JSONObject res = new JSONObject();
        res.put("data", statService.getUserStat().get());
        res.put("not_administrator", false);
        return res;
    }

    /**
     * handle the request "/getReal" and return the information visit logs.
     * It can return the latest 5 visit logs of the user.
     *
     * @param jwt the jwt in requestHeader used for getting the user's id
     * @return {data: {
     * logs: [
     * {
     * shortUrl: String,
     * long: String,
     * ip: String,
     * source: String,
     * time: Date,
     * },
     * {……},
     * ……
     * ],
     * }
     * }
     */
    @CrossOrigin
    @RequestMapping("/getReal")
    public JSONObject getReal(@RequestHeader("Authorization") String jwt) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        long id = Long.parseLong(c.get("id").toString());

        JSONArray logs = new JSONArray();

        List<ShortenLog> shortenLogs = statService.getUserShortenLogs(id).get();
        List<Long> shortenLogID = new ArrayList<>();
        for (ShortenLog s : shortenLogs) shortenLogID.add(s.getId());

        List<Shortener> shorteners = statService.getUserShorteners(shortenLogID).get();
        List<String> shortenStrings = new ArrayList<>();
        for (Shortener s : shorteners)
            if (!s.getLongUrl().equals("BANNED")) shortenStrings.add(s.getId());

        List<VisitLog> visitLogList = visitLogService.findTop5ByShortenerIdOrderByVisitTimeDesc(shortenStrings).get();

        List<String> longUrls = new ArrayList<>();
        List<String> shortUrls = new ArrayList<>();
        for (VisitLog visitLog : visitLogList) {
            String shortenerIDOfVisitLog = visitLog.getShortenerId();
            for (Shortener s : shorteners) {
                if (s.getId().equals(shortenerIDOfVisitLog)) {
                    longUrls.add(longUrls.size(), s.getLongUrl());
                    long shortenLogId = s.getShortenId();
                    for (ShortenLog shortenLog : shortenLogs) {
                        if (shortenLog.getId() == shortenLogId) {
                            shortUrls.add(shortUrls.size(), shortenLog.getShortUrl());
                            break;
                        }
                    }
                    break;
                }
            }
        }

        int index = 0;
        for (VisitLog visitLog : visitLogList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
            JSONObject tmp = new JSONObject();
            tmp.put("shortUrl", shortUrls.get(index));
            tmp.put("long", longUrls.get(index++));
            tmp.put("ip", visitLog.getIp());
            tmp.put("source", visitLog.getDevice() ? "移动端" : "PC端");
            tmp.put("time", simpleDateFormat.format(visitLog.getVisitTime()));
            logs.add(tmp);
        }

        JSONObject res = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("logs", logs);
        res.put("data", data);
        return res;
    }

    /**
     * handle the request "/getTopTen" and return the information of Urls.
     * It can return the top ten short urls with visit count.
     * It can only be requested by administrators.
     *
     * @param jwt the jwt in requestHeader used for checking the user's type
     * @return {data:[
     * {
     * shortUrl:String,
     * longUrl:JSONArray
     * count:Long,
     * },
     * {……},
     * ……
     * ],
     * not_administrator:Boolean
     * }
     * <p>
     * sample of longUrl
     * longUrl:[
     * {
     * url: 'https://sample.url/what'
     * },
     * {
     * url: 'blabla'
     * },……]
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getTopTen")
    public JSONObject getTopTen(@RequestHeader("Authorization") String jwt) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        if ((int) c.get("role") != 0) {
            JSONObject res = new JSONObject();
            res.put("not_administrator", true);
            return res;
        }
        List<ShortenLog> shortenLogList = shortenLogService.findTopTenOrderByVisitCount().get();
        JSONArray data = new JSONArray();
        for (ShortenLog shortenLog : shortenLogList) {
            List<Shortener> shortenerList = shortenLog.getShortener();
            JSONArray longUrls = new JSONArray();
            for (Shortener shortener : shortenerList) {
                JSONObject tmp = new JSONObject();
                tmp.put("url", shortener.getLongUrl());
                longUrls.add(tmp);
            }
            JSONObject tmp = new JSONObject();
            tmp.put("shortUrl", shortenLog.getShortUrl());
            tmp.put("longUrl", longUrls);
            tmp.put("count", shortenLog.getVisitCount());
            data.add(tmp);
        }
        JSONObject res = new JSONObject();
        res.put("data", data);
        res.put("not_administrator", false);
        return res;
    }

    /**
     * handle the request "/getAllUrls" and return the statistics of all Urls.
     * It's similar to "/getStat"
     * It can only be requested by administrators.
     *
     * @param jwt the jwt in requestHeader used for checking the user's type
     * @return {data:[
     * {
     * shortUrl:String,
     * longUrl:JSONArray
     * count:Integer,
     * creatorName:String,
     * createTime:String
     * },
     * {……},
     * ……
     * ],
     * not_administrator: Boolean
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getAllUrls")
    public JSONObject getAllUrls(@RequestHeader("Authorization") String jwt) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        if ((int) c.get("role") != 0) {
            JSONObject res = new JSONObject();
            res.put("not_administrator", true);
            return res;
        }
        JSONObject res = new JSONObject();
        res.put("data", statService.getAllUrls().get());
        res.put("not_administrator", false);
        return res;
    }

    /**
     * handle the request "/getAllUrlsPageable" and return the statistics of pageable Urls.
     * It's similar to "/getStat"
     * It can only be requested by administrators.
     *
     * @param jwt the jwt in requestHeader used for checking the user's type
     * @return {data:[
     * {
     * shortUrl:String,
     * longUrl:JSONArray
     * count:Integer,
     * creatorName:String,
     * createTime:String
     * },
     * {……},
     * ……
     * ],
     * not_administrator: Boolean
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getAllUrlsPageable")
    public JSONObject getAllUrlsPageable(@RequestHeader("Authorization") String jwt, @RequestParam("pageCount") int pageCount, @RequestParam("pageSize") int pageSize) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        if ((int) c.get("role") != 0) {
            JSONObject res = new JSONObject();
            res.put("not_administrator", true);
            return res;
        }
        JSONObject res = new JSONObject();
        Pageable pageable = PageRequest.of(pageCount, pageSize);
        res.put("data", statService.getPagedUrls(pageable).get());
        res.put("not_administrator", false);
        return res;
    }

    /**
     * handle the request "/getNumberCount" and return general statistics of the whole system.
     * It can only be requested by administrators.
     *
     * @param jwt the jwt in requestHeader used for checking the user's type
     * @return {data:{
     * userCount:Integer,
     * shortUrlCount:Integer,
     * visitCountTotal:Integer,
     * shortUrl:String,
     * },
     * not_administrator: Boolean
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getNumberCount")
    public JSONObject getNumberCount(@RequestHeader("Authorization") String jwt) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        if ((int) c.get("role") != 0) {
            JSONObject res = new JSONObject();
            res.put("not_administrator", true);
            return res;
        }
        JSONObject res = new JSONObject();
        res.put("data", statService.getNumberCount().get());
        res.put("not_administrator", false);
        return res;
    }
}
