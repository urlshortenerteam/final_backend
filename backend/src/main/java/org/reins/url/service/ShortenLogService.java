package org.reins.url.service;

import org.reins.url.entity.ShortenLog;

import java.util.List;

public interface ShortenLogService {
    void addShortenLog(long creatorId, List<String> shortUrls, List<String> longUrls);

    void changeShortenLog(ShortenLog shortenLog);

    ShortenLog findByShortUrl(String shortUrl);
}
