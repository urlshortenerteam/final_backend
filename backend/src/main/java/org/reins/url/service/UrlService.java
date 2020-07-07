package org.reins.url.service;
public interface UrlService {
    String generateShortUrl(String longUrl);
    String getLongUrl(String shortUrl);
}
