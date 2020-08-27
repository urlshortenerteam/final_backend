package org.reevoo.url.service;

import org.reevoo.url.entity.VisitLog;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface VisitLogService {
    CompletableFuture<List<VisitLog>> findTop5ByVisitTime();
}
