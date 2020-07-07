package org.reins.url.service;
import java.util.List;
public interface UrlService {
<<<<<<< HEAD

    String generateShortUrl(Integer id,String longUrl);
=======
    String generateShortUrl(String longUrl);
>>>>>>> master
    String getLongUrl(String shortUrl);
    String generateOneShortUrl(Integer id,List<String> longUrls);
}
