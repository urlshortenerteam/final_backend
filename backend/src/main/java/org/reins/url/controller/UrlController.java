package org.reins.url.controller;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import net.sf.json.JSONObject;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import org.reins.url.entity.Users;
import org.reins.url.service.Shorten_logService;
import org.reins.url.service.ShortenerService;
import org.reins.url.service.UsersService;
import org.reins.url.service.Visit_logService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UrlController {
    @Autowired
    private Shorten_logService shorten_logService;
    @Autowired
    private ShortenerService shortenerService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private Visit_logService visit_logService;

    private String long2short(String longUrl) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * chars.length());
            key.append(chars, index, index + 1);
        }
        String hex = DigestUtils.md5DigestAsHex((key.toString() + longUrl).getBytes());
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            long hexLong = 0x3fffffff & Long.parseLong(hex.substring(i * 8, i * 8 + 8), 16);
            StringBuilder outChars = new StringBuilder();
            for (int j = 0; j < 6; j++) {
                long index = 0x3d & hexLong;
                outChars.append(chars, (int) index, (int) index + 1);
                hexLong >>= 5;
            }
            res.add(outChars.toString());
        }
        return res.get((int) (Math.random() * 4));
    }

    @CrossOrigin
    @RequestMapping("/getShort")
    public Map<String, List<String>> generateShort(@RequestParam("id") long id, @RequestBody List<String> longUrls) {
        List<String> shortUrls = new ArrayList<>();
        for (String longUrl : longUrls) {
            shortUrls.add(long2short(longUrl));
        }
        shorten_logService.addShorten_log(id, shortUrls, longUrls);
        Map<String, List<String>> res = new HashMap<>();
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
        List<Shortener> longUrls = shortenerService.findShortenerByShort_url(shortUrl);
        if (longUrls.isEmpty()) return;
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
    public Map<String, Boolean> editUrl(@Param("id") long id, @Param("shortUrl") String shortUrl, @RequestBody String longUrl) {
        List<Shortener> shortenerList = shortenerService.findShortenerByShort_url(shortUrl);
        Map<String, Boolean> res = new HashMap<>();
        if (shortenerList.size() > 1) {
            res.put("data", false);
            return res;
        }
        Shortener shortener = shortenerList.get(0);
        Shorten_log shorten_log = shorten_logService.findById(shortener.getShorten_id());
        if (shorten_log == null) {
            res.put("data", false);
            return res;
        }
        Users users = usersService.findById(id);
        if (users != null && (shorten_log.getCreator_id() == id || (users.getRole() == 0 && longUrl.equals("BANNED")))) {
            shortener.setLong_url(longUrl);
            shortenerService.changeLong_url(shortener);
            res.put("data", true);
            return res;
        }
        res.put("data", false);
        return res;
    }
}
