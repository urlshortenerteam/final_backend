package org.reins.url.service;

import java.util.List;

public interface UrlService {

    String generateShortUrl(String longUrl);
    String getLongUrl(String shortUrl);
    String generateOneShortUrl(List<String> longUrls);
}
