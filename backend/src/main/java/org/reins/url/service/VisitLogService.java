package org.reins.url.service;

import org.reins.url.entity.VisitLog;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface VisitLogService {
    void addVisitLog(String shortenerId, String ip, Boolean device);

    CompletableFuture<List<VisitLog>> findAllOrderByVisitTime();
}
