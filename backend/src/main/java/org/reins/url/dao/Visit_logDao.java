package org.reins.url.dao;

import org.reins.url.entity.Visit_log;

import java.util.List;

public interface Visit_logDao {
    void addVisit_log(String shortener_id, String ip, Boolean device);

    List<Visit_log> findByShortenerId(String shortener_id);
}
