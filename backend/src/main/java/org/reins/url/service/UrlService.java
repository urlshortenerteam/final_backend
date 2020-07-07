package org.reins.url.service;

import java.util.List;

public interface UrlService {

    String generateShortUrl(Integer id,String longUrl);
    String getLongUrl(String shortUrl);
    String generateOneShortUrl(Integer id,List<String> longUrls);
}
