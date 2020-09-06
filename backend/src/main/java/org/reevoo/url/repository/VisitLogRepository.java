package org.reevoo.url.repository;

import org.reevoo.url.entity.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
    List<VisitLog> findByShortenerId(String shortenerId);

    List<VisitLog> findTop5ByShortenerIdInOrderByVisitTimeDesc(List<String> shortenerId);
}
