package org.reevoo.url.service;

import org.reevoo.url.entity.VisitLog;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface VisitLogService {
    CompletableFuture<List<VisitLog>> findOrderByVisitTimePageable(Pageable pageable);
}
