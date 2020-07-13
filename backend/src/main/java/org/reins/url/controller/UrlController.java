package org.reins.url.controller;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import org.reins.url.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
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
    UrlService urlService;
    private String long2short(String longUrl) {
        String key="azhe";
        String chars="abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String hex=DigestUtils.md5DigestAsHex((key+longUrl).getBytes());
        List<String> res=new ArrayList<>();
        for (int i=0;i<4;i++) {
            long hexLong=0x3fffffff&Long.parseLong(hex.substring(i*8,i*8+8),16);
            StringBuilder outChars=new StringBuilder();
            for (int j=0;j<6;j++) {
                long index=0x3d&hexLong;
                outChars.append(chars,(int)index,(int)index+1);
                hexLong>>=5;
            }
            res.add(outChars.toString());
        }
        return res.get((int)(Math.random()*4));
    }
    @CrossOrigin
    @RequestMapping("/getShort")
    public Map<String,List<String>> generateShort(@RequestParam("id") long id,@RequestBody List<String> longUrls) {
        List<String> shortUrls=new ArrayList<>();
        for (int i=0;i<longUrls.size();i++) {
            String longUrl=longUrls.get(i);
            shortUrls.add(long2short(longUrl));
        }
        urlService.addShortenLog(id,shortUrls,longUrls);
        Map<String,List<String>> res=new HashMap<>();
        res.put("data",shortUrls);
        return res;
    }
    @CrossOrigin
    @RequestMapping("/getOneShort")
    public Map<String,String> generateOneShort(@RequestParam("id") long id,@RequestBody List<String> longUrls) {
        String longUrl=longUrls.get((int)(Math.random()*longUrls.size()));
        String shortUrl=long2short(longUrl);
        List<String> shortUrls=new ArrayList<>();
        for (int i=0;i<longUrls.size();++i) shortUrls.add(shortUrl);
        urlService.addShortenLog(id,shortUrls,longUrls);
        Map<String,String> res=new HashMap<>();
        res.put("data",shortUrl);
        return res;
    }
    @CrossOrigin
    @RequestMapping("/{[A-Za-z0-9]{6}}")
    public void getLong(HttpServletRequest req,HttpServletResponse resp) {
        String shortUrl=req.getRequestURI().substring(1);
        List<Shorten_log> shorten_logList=urlService.getShortenLog();
        List<Shortener> longUrls=new ArrayList<>();
        for (int i=0;i<shorten_logList.size();i++) {
            List<Shortener> shortenerList=shorten_logList.get(i).getShortener();
            for (int j=0;j<shortenerList.size();j++) {
                Shortener shortener=shortenerList.get(j);
                if (shortener.getShort_url().equals(shortUrl)) longUrls.add(shortener);
            }
        }
        if (longUrls.isEmpty()) return;
        Shortener longUrl=longUrls.get((int)(Math.random()*longUrls.size()));
        Boolean device=(UserAgent.parseUserAgentString(req.getHeader("User-Agent")).getOperatingSystem().getDeviceType()!=DeviceType.COMPUTER);
        try {
            urlService.addVisitLog(longUrl.getId(),req.getRemoteAddr(),device);
            resp.sendRedirect(longUrl.getLong_url());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
