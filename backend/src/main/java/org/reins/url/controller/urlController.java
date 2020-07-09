package org.reins.url.controller;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import org.reins.url.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
@RestController
public class urlController {
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
    @RequestMapping("/getShort")
    public List<String> generateShort(@RequestParam("id") long id,@RequestBody List<String> longUrls) {
        List<String> shortUrls=new ArrayList<>();
        for (int i=0;i<longUrls.size();i++) {
            String longUrl=longUrls.get(i);
            shortUrls.add(long2short(longUrl));
        }
        urlService.addLog(id,shortUrls,longUrls);
        return shortUrls;
    }
    @RequestMapping("/getOneShort")
    public String generateOneShort(@RequestParam("id") long id,@RequestBody List<String> longUrls) {
        String longUrl=longUrls.get((int)(Math.random()*longUrls.size()));
        String shortUrl=long2short(longUrl);
        List<String> shortUrls=new ArrayList<>();
        for (int i=0;i<longUrls.size();++i) shortUrls.add(shortUrl);
        urlService.addLog(id,shortUrls,longUrls);
        return shortUrl;
    }
    @RequestMapping("/getLong")
    public String getLong(@RequestParam("shortUrl") String shortUrl) {
        List<Shorten_log> shorten_logList=urlService.getLog();
        List<String> longUrls=new ArrayList<>();
        for (int i=0;i<shorten_logList.size();i++) {
            List<Shortener> shortenerList=shorten_logList.get(i).getShortener();
            for (int j=0;j<shortenerList.size();j++) {
                Shortener shortener=shortenerList.get(j);
                if (shortener.getShort_url().equals(shortUrl)) longUrls.add(shortener.getLong_url());
            }
        }
        if (longUrls.isEmpty()) return null;
        return longUrls.get((int)(Math.random()*longUrls.size()));
    }
}
