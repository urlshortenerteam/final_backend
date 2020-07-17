package org.reins.url.controller;

import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import org.reins.url.entity.Shorten_log;
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
    private Edit_logService edit_logService;
    @Autowired
    private Shorten_logService shorten_logService;
    @Autowired
    private ShortenerService shortenerService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private Visit_logService visit_logService;

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
        for (String longUrl : longUrls) {
            shortUrls.add(long2short(longUrl));
        }
        shorten_logService.addShorten_log(id, shortUrls, longUrls);
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
        for (int i = 0; i < longUrls.size(); ++i) shortUrls.add(shortUrl);
        shorten_logService.addShorten_log(id, shortUrls, longUrls);
        JSONObject res = new JSONObject();
        res.put("data", shortUrl);
        return res;
    }

    @CrossOrigin
    @RequestMapping("/{[A-Za-z0-9]{6}}")
    public void getLong(HttpServletRequest req, HttpServletResponse resp) {
        String shortUrl = req.getRequestURI().substring(1);
        List<Shortener> longUrls = shortenerService.findByShort_url(shortUrl);
        if (longUrls.isEmpty() || longUrls.get(0).getLong_url().equals("BANNED")) return;
        Shortener longUrl = longUrls.get((int) (Math.random() * longUrls.size()));
        Shorten_log shorten_log = shorten_logService.findById(longUrl.getShorten_id());
        if (shorten_log == null) return;
        Boolean device = (UserAgent.parseUserAgentString(req.getHeader("User-Agent")).getOperatingSystem().getDeviceType() != DeviceType.COMPUTER);
        try {
            visit_logService.addVisit_log(longUrl.getId(), req.getRemoteAddr(), device);
            usersService.changeVisit_count(shorten_log.getCreator_id());
            resp.sendRedirect(longUrl.getLong_url());
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
        List<Shortener> shortenerList = shortenerService.findByShort_url(shortUrl);
        if (user == null || shortenerList.isEmpty()) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        Shortener shortener = shortenerList.get(0);
        Shorten_log shorten_log = shorten_logService.findById(shortener.getShorten_id());
        if (shorten_log == null) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        if (longUrl.equals("BANNED") || longUrl.equals("LIFT")) {
            boolean ban = longUrl.equals("BANNED");
            boolean shortenerBan = shortener.getLong_url().equals("BANNED");
            if ((user.getRole() > 0 && id != shorten_log.getCreator_id()) || (ban && shortenerBan) || (!ban && !shortenerBan)) {
                status.put("status", false);
                res.put("data", status);
                return res;
            }
            if (ban) shortenerService.addShortener(shorten_log.getId(), shortUrl, longUrl);
            else shortenerService.deleteShortener(shortener.getId());
            edit_logService.addEdit_log(id, shortener.getId());
            status.put("status", true);
            res.put("data", status);
            return res;
        }
        if (shortenerList.size() > 1 || id != shorten_log.getCreator_id()) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        shortener.setLong_url(longUrl);
        shortenerService.changeLong_url(shortener);
        edit_logService.addEdit_log(id, shortener.getId());
        status.put("status", true);
        res.put("data", status);
        return res;
    }
}
