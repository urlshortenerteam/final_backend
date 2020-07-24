package org.reins.url.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
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
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    res.put("data", shortenLogService.addShortenLog(Long.parseLong(c.get("id").toString()), longUrls));
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
    res.put("data", shortenLogService.addOneShortenLog(Long.parseLong(c.get("id").toString()), longUrls));
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
        try {
            shortenLog.setVisitCount(shortenLog.getVisitCount() + 1);
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
   * @param jwt      the jwt in requestHeader used for checking the user's id
     * @param shortUrl the short url used to find the shorten log
     * @param longUrl  the long url that needs to be edited
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
        String longUrl = JSON.parse(params.get("longUrl")).toString();
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
