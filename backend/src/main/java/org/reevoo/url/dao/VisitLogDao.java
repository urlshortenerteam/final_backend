package org.reevoo.url.dao;

import org.reevoo.url.entity.VisitLog;

import java.util.List;

public interface VisitLogDao {
    List<VisitLog> findAllOrderByVisitTime();

    List<VisitLog> findByShortenerId(String shortenerId);
}
