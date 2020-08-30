package org.reevoo.url.dao;

import org.reevoo.url.entity.VisitLog;

import java.util.List;

public interface VisitLogDao {
    List<VisitLog> findByShortenerId(String shortenerId);

    List<VisitLog> findTop5ByShortenerIdOrderByVisitTimeDesc(List<String> shortenerId);
}
