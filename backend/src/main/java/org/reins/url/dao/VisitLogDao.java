package org.reins.url.dao;

import org.reins.url.entity.VisitLog;

import java.util.List;

public interface VisitLogDao {
    List<VisitLog> findAllOrderByVisitTime();

    List<VisitLog> findByShortenerId(String shortenerId);
}
