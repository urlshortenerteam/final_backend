package org.reins.url.dao;
import org.reins.url.entity.Shorten_log;
import java.util.List;
public interface UrlDao {
    void addLog(long creator_id,List<String> shortUrls,List<String> longUrls);
    List<Shorten_log> getLog();
}
