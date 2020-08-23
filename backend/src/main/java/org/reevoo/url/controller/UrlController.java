package org.reevoo.url.controller;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.reevoo.url.util.JwtUtil;
import org.reevoo.url.entity.ShortenLog;
import org.reevoo.url.entity.Shortener;
import org.reevoo.url.entity.Users;
import org.reevoo.url.service.EditLogService;
import org.reevoo.url.service.ShortenLogService;
import org.reevoo.url.service.ShortenerService;
import org.reevoo.url.service.UsersService;
import org.reevoo.url.service.VisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     * @param jwt      the jwt in requestHeader used for checking the user's id
     * @param longUrls the long urls that needs to be generated to short urls
     * @return {data:[
     * "000000",
     * ...
     * ]
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getShort")
    public JSONObject generateShort(@RequestHeader("Authorization") String jwt, @RequestBody List<String> longUrls) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        JSONObject res = new JSONObject();
        res.put("data", shortenLogService.addShortenLog(Long.parseLong(c.get("id").toString()), longUrls).get());
        return res;
    }

    /**
     * handle the request "/getOneShort" and generate one short url.
     *
     * @param jwt      the jwt in requestHeader used for checking the user's id
     * @param longUrls the long urls that needs to be generated to the short url
     * @return {data:
     * shortUrl
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getOneShort")
    public JSONObject generateOneShort(@RequestHeader("Authorization") String jwt, @RequestBody List<String> longUrls) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        JSONObject res = new JSONObject();
        res.put("data", shortenLogService.addOneShortenLog(Long.parseLong(c.get("id").toString()), longUrls).get());
        return res;
    }

    /**
     * handle the request "/editUrl" and edit the information of Urls.
     *
     * @param jwt    the jwt in requestHeader used for checking the user's id
     * @param params the params includes shortUrl and longUrl
     * @return {data:{
     * status:Boolean
     * }
     * }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/editUrl")
    public JSONObject editUrl(@RequestHeader("Authorization") String jwt, @RequestBody Map<String, String> params) throws Exception {
        Claims c = JwtUtil.parseJWT(jwt);
        long id = Long.parseLong(c.get("id").toString());
        String shortUrl = params.get("shortUrl");
        String longUrl = params.get("longUrl");
        JSONObject res = new JSONObject();
        JSONObject status = new JSONObject();
        Users user = usersService.findById(id).get();
        ShortenLog shortenLog = shortenLogService.findByShortUrl(shortUrl).get();
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
