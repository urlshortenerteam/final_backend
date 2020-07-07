package org.reins.url.service;

public interface UrlService {

    String generateShorten(String longUrl);
    String getLongUrl(String shorten);
}
