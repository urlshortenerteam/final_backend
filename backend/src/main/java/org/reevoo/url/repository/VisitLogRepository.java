package org.reevoo.url.repository;

import org.reevoo.url.entity.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
    @Query("select v from VisitLog v where v.shortenerId=:shortenerId")
    List<VisitLog> findByShortenerId(@Param("shortenerId") String shortenerId);

    List<VisitLog> findTop5ByShortenerIdInOrderByVisitTimeDesc(List<String> shortenerId);
}
