package org.reins.url.service;

import org.reins.url.entity.VisitLog;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface VisitLogService {
    CompletableFuture<List<VisitLog>> findAllOrderByVisitTime();
}
