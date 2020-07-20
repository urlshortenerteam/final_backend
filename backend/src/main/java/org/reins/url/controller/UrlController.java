package org.reins.url.controller;

import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import org.reins.url.entity.ShortenLog;
import org.reins.url.entity.Shortener;
import org.reins.url.entity.Users;
import org.reins.url.service.*;
import org.reins.url.xeger.Xeger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public String long2short(String longUrl) {
        Xeger xeger = new Xeger("[A-Za-z0-9]{6}", new Random(0));
        String hex = DigestUtils.md5DigestAsHex((xeger.generate() + longUrl).getBytes());
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            long hexLong = 0x3fffffff & Long.parseLong(hex.substring(i * 8, i * 8 + 8), 16);
            StringBuilder outChars = new StringBuilder();
            for (int j = 0; j < 6; j++) {
                long index = 0x3d & hexLong;
                if (index < 26) outChars.append((char) ('A' + index));
                else if (index < 52) outChars.append((char) ('a' + index - 26));
                else outChars.append((char) ('0' + index - 52));
                hexLong >>= 5;
            }
            res.add(outChars.toString());
        }
        return res.get((int) (Math.random() * 4));
    }

    @CrossOrigin
    @RequestMapping("/getShort")
    public JSONObject generateShort(@RequestParam("id") long id, @RequestBody List<String> longUrls) {
        List<String> shortUrls = new ArrayList<>();
        for (String longUrl : longUrls) shortUrls.add(long2short(longUrl));
        shortenLogService.addShortenLog(id, shortUrls, longUrls);
        JSONObject res = new JSONObject();
        res.put("data", shortUrls);
        return res;
    }

    @CrossOrigin
    @RequestMapping("/getOneShort")
    public JSONObject generateOneShort(@RequestParam("id") long id, @RequestBody List<String> longUrls) {
        String longUrl = longUrls.get((int) (Math.random() * longUrls.size()));
        String shortUrl = long2short(longUrl);
        List<String> shortUrls = new ArrayList<>();
        for (int i = 0; i < longUrls.size(); i++) shortUrls.add(shortUrl);
        shortenLogService.addShortenLog(id, shortUrls, longUrls);
        JSONObject res = new JSONObject();
        res.put("data", shortUrl);
        return res;
    }

    @CrossOrigin
    @RequestMapping("/{[A-Za-z0-9]{6}}")
    public void getLong(HttpServletRequest req, HttpServletResponse resp) {
        String shortUrl = req.getRequestURI().substring(1);
        ShortenLog shortenLog = shortenLogService.findByShortUrl(shortUrl);
        if (shortenLog == null) return;
        List<Shortener> longUrls = shortenLog.getShortener();
        if (longUrls.isEmpty()) return;
        Shortener longUrl = longUrls.get(0);
        if (!longUrl.getLongUrl().equals("BANNED")) longUrl = longUrls.get((int) (Math.random() * longUrls.size()));
        Boolean device = (UserAgent.parseUserAgentString(req.getHeader("User-Agent")).getOperatingSystem().getDeviceType() != DeviceType.COMPUTER);
        shortenLog.setVisitCount(shortenLog.getVisitCount() + 1);
        try {
            shortenLogService.changeShortenLog(shortenLog);
            usersService.changeVisitCount(shortenLog.getCreatorId());
            visitLogService.addVisitLog(longUrl.getId(), req.getRemoteAddr(), device);
            resp.sendRedirect(longUrl.getLongUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
