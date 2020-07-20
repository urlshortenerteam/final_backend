package org.reins.url.controller;

import com.alibaba.fastjson.JSONObject;
import org.reins.url.entity.ShortenLog;
import org.reins.url.entity.Shortener;
import org.reins.url.entity.VisitLog;
import org.reins.url.service.ShortenLogService;
import org.reins.url.service.ShortenerService;
import io.jsonwebtoken.Claims;
import org.reins.url.service.StatService;
import org.reins.url.service.VisitLogService;
import org.reins.url.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletRequestAttributes;

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

  /**
   * handle the request "/getStat" and return the statistics of the user's Urls.
   *
   * @param id the id of the user who calls "/getStat"
   * @return {data:
   * [
   * {
   * shortUrl:String,
   * longUrl:JSONArray
   * count:Integer,
   * area_distr:JSONArray,
   * time_distr:JSONArray,
   * source_distr:JSONArray
   * },
   * {……},
   * ……
   * ]
   * }
   */
  @CrossOrigin
  @RequestMapping("/getStat")
  public JSONObject getStat(@RequestParam("id") long id) {
    JSONObject res = new JSONObject();
    res.put("data", statService.getStat(id));
    return res;
  }

  /**
   * handle the request "/getShortStat" and return the statistics of a single Url.
   *
   * @param id        the id of the user who calls "/getShortStat"
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
   */
  @CrossOrigin
  @RequestMapping("/getShortStat")
  public JSONObject getShortStat(@RequestParam("id") long id, @RequestParam("short") String short_url) {
    JSONObject res = new JSONObject();
    res.put("data", statService.getShortStat(short_url));
    return res;
  }

  /**
   * handle the request "/getUserStat" and return the information of all users.
   * It can only be requested by administrators.
   *
   */
  @CrossOrigin
  @RequestMapping("/getUserStat")
  public JSONObject getUserStat(@RequestHeader("Authorization") String jwt) throws Exception {
    if (!jwt.equals("SXSTQL")) {
      Claims c = JwtUtil.parseJWT(jwt);
      if ((int) c.get("role") != 0) {
        JSONObject res = new JSONObject();
        res.put("not_administrator", true);
        return res;
      }
    }

    JSONObject res = new JSONObject();
    res.put("data", statService.getUserStat());
    res.put("not_administrator", false);
    return res;
  }

  /**
   * .
   */
  @CrossOrigin
  @RequestMapping("/getAllUrls")
  public JSONObject getAllUrls(@RequestHeader("Authorization") String jwt) throws Exception {
    if (!jwt.equals("SXSTQL")) {
      Claims c = JwtUtil.parseJWT(jwt);
      if ((int) c.get("role") != 0) {
        JSONObject res = new JSONObject();
        res.put("not_administrator", true);
        return res;
      }
    }

    JSONObject res = new JSONObject();
    res.put("data", statService.getAllUrls());
    res.put("not_administrator", false);
    return res;
  }

  /**
   * .
   */
  @CrossOrigin
  @RequestMapping("/getNumberCount")
  public JSONObject getNumberCount(@RequestHeader("Authorization") String jwt) throws Exception {
    if (!jwt.equals("SXSTQL")) {
      Claims c = JwtUtil.parseJWT(jwt);
      if ((int) c.get("role") != 0) {
        JSONObject res = new JSONObject();
        res.put("not_administrator", true);
        return res;
      }
    }

    JSONObject res = new JSONObject();
    res.put("data", statService.getNumberCount());
    res.put("not_administrator", false);
    return res;
  }


}
