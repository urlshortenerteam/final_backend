package org.reins.url.controller;

import net.sf.json.JSONObject;
import org.reins.url.entity.Statistics;
import org.reins.url.entity.Users;
import org.reins.url.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StatController {
    @Autowired
    private StatService statService;

    @CrossOrigin
    @RequestMapping("/getStat")
    public JSONObject getStat(@RequestParam("id") long id) {
        JSONObject res = new JSONObject();
        res.put("data", statService.getStat());
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
}
