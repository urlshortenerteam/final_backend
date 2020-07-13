package org.reins.url.service;
import org.reins.url.entity.Shorten_log;
import java.util.List;
public interface Shorten_logService {
    void addShorten_log(long creator_id,List<String> shortUrls,List<String> longUrls);
    Shorten_log findById(long id);
}
