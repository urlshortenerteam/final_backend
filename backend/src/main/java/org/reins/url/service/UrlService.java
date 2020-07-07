package org.reins.url.service;
import org.reins.url.entity.Shorten_log;
import java.util.List;
public interface UrlService {
<<<<<<< HEAD
<<<<<<< HEAD

    String generateShortUrl(Integer id,String longUrl);
=======
    String generateShortUrl(String longUrl);
>>>>>>> master
    String getLongUrl(String shortUrl);
    String generateOneShortUrl(Integer id,List<String> longUrls);
=======
    void addLog(long creator_id,List<String> shortUrls,List<String> longUrls);
    List<Shorten_log> getLog();
>>>>>>> master
}
