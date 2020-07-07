package org.reins.url.controller;
import org.reins.url.entity.Statistics;
import org.reins.url.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
public class statController {
    @Autowired StatService statService;

    @RequestMapping("/getStat")
    public List<Statistics> getStat(@RequestParam("user_id") Integer user_id){}

    @RequestMapping("/getShortStat")
    public Statistics getShortStat(@RequestParam("user_id") Integer user_id,@RequestParam("short_url") String short_url){}
}
