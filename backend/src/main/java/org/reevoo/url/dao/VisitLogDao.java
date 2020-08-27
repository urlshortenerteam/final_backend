package org.reevoo.url.dao;

import org.reevoo.url.entity.VisitLog;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface VisitLogDao {
    List<VisitLog> findOrderByVisitTime(Pageable pageable);

    List<VisitLog> findByShortenerId(String shortenerId);
}
