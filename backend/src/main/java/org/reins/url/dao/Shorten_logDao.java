package org.reins.url.dao;

import org.reins.url.entity.Shorten_log;

import java.util.List;

public interface Shorten_logDao {
    void addShorten_log(long creator_id, List<String> shortUrls, List<String> longUrls);

    Shorten_log findById(long id);

    List<Shorten_log> findByCreator_id(long creator_id);
}
