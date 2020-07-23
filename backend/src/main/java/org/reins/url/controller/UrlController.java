package org.reins.url.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import org.reins.url.entity.ShortenLog;
import org.reins.url.entity.Shortener;
import org.reins.url.entity.Users;
import org.reins.url.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class UrlController {
    @Autowired
    private EditLogService editLogService;
    @Autowired
    private ShortenerService shortenerService;
    @Autowired
    private ShortenLogService shortenLogService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private VisitLogService visitLogService;

    /**
     * handle the request "/getShort" and generate short urls.
     *
     * @param id       the id in requestParam used for checking the user's id
     * @param longUrls the long urls that needs to be generated to short urls
     * @return {data:[
     * "000000",
     * ...
     * ]
     * }
     */
    @CrossOrigin
    @RequestMapping("/getShort")
    public JSONObject generateShort(@RequestParam("id") long id, @RequestBody List<String> longUrls) {
        JSONObject res = new JSONObject();
        res.put("data", shortenLogService.addShortenLog(id, longUrls));
        return res;
    }

    /**
     * handle the request "/getOneShort" and generate one short url.
     *
     * @param id       the id in requestParam used for checking the user's id
     * @param longUrls the long urls that needs to be generated to the short url
     * @return {data:
     * shortUrl
     * }
     */
    @CrossOrigin
    @RequestMapping("/getOneShort")
    public JSONObject generateOneShort(@RequestParam("id") long id, @RequestBody List<String> longUrls) {
        JSONObject res = new JSONObject();
        res.put("data", shortenLogService.addOneShortenLog(id, longUrls));
        return res;
    }

    /**
     * handle the request "/{[A-Za-z0-9]{6}}" and redirect to the long url.
     */
    @CrossOrigin
    @RequestMapping("/{[A-Za-z0-9]{6}}")
    public void getLong(HttpServletRequest req, HttpServletResponse resp) {
        String shortUrl = req.getRequestURI().substring(1);
        ShortenLog shortenLog = shortenLogService.findByShortUrl(shortUrl);
        if (shortenLog == null) {
            try {
                resp.sendRedirect("/static/error.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        List<Shortener> longUrls = shortenLog.getShortener();
        if (longUrls.isEmpty()) {
            try {
                resp.sendRedirect("/static/error.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        Shortener longUrl = longUrls.get(0);
        if (!longUrl.getLongUrl().equals("BANNED")) longUrl = longUrls.get((int) (Math.random() * longUrls.size()));
        Boolean device = (UserAgent.parseUserAgentString(req.getHeader("User-Agent")).getOperatingSystem().getDeviceType() != DeviceType.COMPUTER);
        shortenLog.setVisitCount(shortenLog.getVisitCount() + 1);
        try {
            shortenLogService.changeShortenLog(shortenLog);
            usersService.changeVisitCount(shortenLog.getCreatorId());
            visitLogService.addVisitLog(longUrl.getId(), req.getRemoteAddr(), device);
            if (longUrl.getLongUrl().equals("BANNED")) resp.sendRedirect("/static/banned.html");
            else resp.sendRedirect(longUrl.getLongUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handle the request "/editUrl" and edit the information of Urls.
     *
     * @param id       the id in requestParam used for checking the user's id
     * @param shortUrl the short url used to find the shorten log
     * @param longUrl  the long url that needs to be edited
     * @return {data:{
     * status:Boolean
     * }
     * }
     */
    @CrossOrigin
    @RequestMapping("/editUrl")
    public JSONObject editUrl(@Param("id") long id, @Param("shortUrl") String shortUrl, @RequestBody String longUrl) {
        JSONObject res = new JSONObject();
        JSONObject status = new JSONObject();
        Users user = usersService.findById(id);
        ShortenLog shortenLog = shortenLogService.findByShortUrl(shortUrl);
        if (shortenLog == null || user == null) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        List<Shortener> longUrls = shortenLog.getShortener();
        if (longUrls.isEmpty()) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        Shortener shortener = longUrls.get(0);
        longUrl = JSON.parse(longUrl).toString();
        if (longUrl.equals("BANNED") || longUrl.equals("LIFT")) {
            boolean ban = longUrl.equals("BANNED");
            boolean shortenerBan = shortener.getLongUrl().equals("BANNED");
            if ((user.getRole() > 0 && id != shortenLog.getCreatorId()) || (ban && shortenerBan) || (!ban && !shortenerBan)) {
                status.put("status", false);
                res.put("data", status);
                return res;
            }
            shortenerService.addShortener(id, shortenLog.getId(), longUrl);
            status.put("status", true);
            res.put("data", status);
            return res;
        }
        if (longUrls.size() > 1 || id != shortenLog.getCreatorId()) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        shortener.setLongUrl(longUrl);
        editLogService.addEditLog(id, shortener.getId());
        shortenerService.changeShortener(shortener);
        status.put("status", true);
        res.put("data", status);
        return res;
    }
}
