package org.reins.url.controller;
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
public class UrlController {
    @Autowired
    UrlService urlService;
    @RequestMapping("/getShort")
    public List<String> generateShort(@RequestParam("id") long id,@RequestBody List<String> longUrls) {
        List<String> shortUrls=new ArrayList<>();
        String key="azhe";
        String chars="abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i=0;i<longUrls.size();i++) {
            String longUrl=longUrls.get(i);
            String hex=DigestUtils.md5DigestAsHex((key+longUrl).getBytes());
            List<String> res=new ArrayList<>();
            for (int j=0;j<4;j++) {
                long hexLong=0x3fffffff&Long.parseLong(hex.substring(j*8,j*8+8),16);
                StringBuilder outChars=new StringBuilder();
                for (int k=0;k<6;k++) {
                    long index=0x3d&hexLong;
                    outChars.append(chars.substring((int)index,(int)index+1));
                    hexLong>>=5;
                }
                res.add(outChars.toString());
            }
            shortUrls.add(res.get((int)(Math.random()*4)));
        }
        urlService.addLog(id,shortUrls,longUrls);
        return shortUrls;
    }
    @RequestMapping("/getOneShort")
    public String generateOneShort(@RequestParam("id") long id,@RequestBody List<String> longUrls) {
        String shortUrl;
        String key="azhe";
        String chars="abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String longUrl=longUrls.get((int)(Math.random()*longUrls.size()));
        String hex=DigestUtils.md5DigestAsHex((key+longUrl).getBytes());
        List<String> res=new ArrayList<>();
        for (int i=0;i<4;i++) {
            long hexLong=0x3fffffff&Long.parseLong(hex.substring(i*8,i*8+8),16);
            StringBuilder outChars=new StringBuilder();
            for (int j=0;j<6;j++) {
                long index=0x3d&hexLong;
                outChars.append(chars.substring((int)index, (int)index+1));
                hexLong>>=5;
            }
            res.add(outChars.toString());
        }
        shortUrl=res.get((int)(Math.random()*4));
        List<String> shortUrls=new ArrayList<>();
        for (int i=0;i<longUrls.size();++i) shortUrls.add(shortUrl);
        urlService.addLog(id,shortUrls,longUrls);
        return shortUrl;
    }
}
