package org.reins.url.service;

public interface VisitLogService {
    void addVisitLog(String shortenerId, String ip, Boolean device);
}
