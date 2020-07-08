package org.reins.url.dao;

import org.reins.url.entity.Visit_log;

import java.util.List;

public interface VisitDao {
    List<Visit_log> findByShortenerId(long shortener_id);
}
