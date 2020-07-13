package org.reins.url.controller;
import org.reins.url.entity.Statistics;
import org.reins.url.entity.Users;
import org.reins.url.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
public class StatController {
    @Autowired
    StatService statService;
    @CrossOrigin
    @RequestMapping("/getStat")
    public Map<String,List<Statistics>> getStat(@RequestParam("id") long id) {
        Map<String,List<Statistics>> res=new HashMap<>();
        res.put("data",statService.getStat());
        return res;
    }
    @CrossOrigin
    @RequestMapping("/getShortStat")
    public Map<String,Statistics> getShortStat(@RequestParam("id") long id,@RequestParam("short") String short_url) {
        Map<String,Statistics> res=new HashMap<>();
        res.put("data",statService.getShortStat(short_url));
        return res;
    }
    @CrossOrigin
    @RequestMapping("/getUserStat")
    public Map<String,List<Users>> getUserStat(){
        Map<String,List<Users>> res=new HashMap<>();
        res.put("data",statService.getUserStat());
        return res;
    }
}
