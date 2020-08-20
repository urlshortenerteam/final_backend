package org.reins.url.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import org.reins.url.entity.ShortenLog;
import org.reins.url.entity.Shortener;
import org.reins.url.entity.Users;
import org.reins.url.service.EditLogService;
import org.reins.url.service.ShortenLogService;
import org.reins.url.service.ShortenerService;
import org.reins.url.service.UsersService;
import org.reins.url.service.VisitLogService;
import org.reins.url.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;

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
    public JSONObject generateShort(@RequestHeader("Authorization") final String jwt,
            @RequestBody final List<String> longUrls) throws Exception {
        final Claims c = JwtUtil.parseJWT(jwt);
        final JSONObject res = new JSONObject();
        res.put("data", shortenLogService.addShortenLog(Long.parseLong(c.get("id").toString()), longUrls).get());
        return res;
    }

    /**
     * handle the request "/getOneShort" and generate one short url.
     *
     * @param jwt      the jwt in requestHeader used for checking the user's id
     * @param longUrls the long urls that needs to be generated to the short url
     * @return {data: shortUrl }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/getOneShort")
    public JSONObject generateOneShort(@RequestHeader("Authorization") final String jwt,
            @RequestBody final List<String> longUrls) throws Exception {
        final Claims c = JwtUtil.parseJWT(jwt);
        final JSONObject res = new JSONObject();
        res.put("data", shortenLogService.addOneShortenLog(Long.parseLong(c.get("id").toString()), longUrls).get());
        return res;
    }

    /**
     * handle the request "/{[A-Za-z0-9]{6}}" and redirect to the long url.
     */
    @CrossOrigin
    @RequestMapping("/{[A-Za-z0-9]{6}}")
    public void getLong(final HttpServletRequest req, final HttpServletResponse resp) {
        final String shortUrl = req.getRequestURI().substring(1);
        try {
            resp.sendRedirect("redirect:9090/redirect?short=" + shortUrl);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handle the request "/editUrl" and edit the information of Urls.
     *
     * @param jwt    the jwt in requestHeader used for checking the user's id
     * @param params the params includes shortUrl and longUrl
     * @return {data:{ status:Boolean } }
     * @throws Exception when the string jwt can't be parsed as a JWT
     */
    @CrossOrigin
    @RequestMapping("/editUrl")
    public JSONObject editUrl(@RequestHeader("Authorization") final String jwt,
            @RequestBody final Map<String, String> params) throws Exception {
        final Claims c = JwtUtil.parseJWT(jwt);
        final long id = Long.parseLong(c.get("id").toString());
        final String shortUrl = params.get("shortUrl");
        final String longUrl = params.get("longUrl");
        final JSONObject res = new JSONObject();
        final JSONObject status = new JSONObject();
        final Users user = usersService.findById(id).get();
        final ShortenLog shortenLog = shortenLogService.findByShortUrl(shortUrl).get();
        if (shortenLog == null || user == null) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        final List<Shortener> longUrls = shortenLog.getShortener();
        if (longUrls.isEmpty()) {
            status.put("status", false);
            res.put("data", status);
            return res;
        }
        final Shortener shortener = longUrls.get(0);
        if (longUrl.equals("BANNED") || longUrl.equals("LIFT")) {
            final boolean ban = longUrl.equals("BANNED");
            final boolean shortenerBan = shortener.getLongUrl().equals("BANNED");
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
