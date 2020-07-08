package org.reins.url.dao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import java.util.List;
import java.util.Optional;
public interface StatDao {
    List<Shorten_log> findAll();
    List<Shorten_log> findByCreator_id(long creator_id);
    Optional<Shorten_log> findById(long shorten_id);
    List<Shortener> findShortenerByShortUrl(String short_url);
}
