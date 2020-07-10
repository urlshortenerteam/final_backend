package org.reins.url.service;
import org.reins.url.entity.Shorten_log;
import java.util.List;
public interface UrlService {
    void addShortenLog(long creator_id,List<String> shortUrls,List<String> longUrls);
    void addVisitLog(String shortener_id,String ip,Boolean device);
    List<Shorten_log> getShortenLog();
}
