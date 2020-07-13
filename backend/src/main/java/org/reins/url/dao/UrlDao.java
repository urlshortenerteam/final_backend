package org.reins.url.dao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import java.util.List;
public interface UrlDao {
    void addShortenLog(long creator_id,List<String> shortUrls,List<String> longUrls);
    void changeUsersVisit_count(long id);
    List<Shortener> findShortenerByShort_url(String short_url);
    Shorten_log findShorten_logById(long id);
}
