package org.reevoo.url.repository;

import org.reevoo.url.entity.ShortenLog;
import org.reevoo.url.entity.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
    @Query("select v from VisitLog v where v.shortenerId=:shortenerId")
    List<VisitLog> findByShortenerId(@Param("shortenerId") String shortenerId);

    @Query(value = "select v from VisitLog v order by v.visitTime desc",
            countQuery = "select count(v) from VisitLog v order by v.visitTime desc")
    List<VisitLog> findOrderByVisitTimeDesc(Pageable pageable);

    List<VisitLog> findTop5ByShortenerIdInOrderByVisitTimeDesc(List<String> shortenerId);

}
