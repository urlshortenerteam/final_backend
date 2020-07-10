package org.reins.url.dao;
import org.reins.url.entity.Shorten_log;
import java.util.List;
public interface UrlDao {
    void addShortenLog(long creator_id,List<String> shortUrls,List<String> longUrls);
    List<Shorten_log> getShortenLog();
}
