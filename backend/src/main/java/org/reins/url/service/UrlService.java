package org.reins.url.service;
import org.reins.url.entity.Shorten_log;
import java.util.List;
public interface UrlService {
    void addLog(long creator_id,List<String> shortUrls,List<String> longUrls);
    List<Shorten_log> getLog();
}
