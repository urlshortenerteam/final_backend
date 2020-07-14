package org.reins.url.service;

public interface Visit_logService {
    void addVisit_log(String shortener_id, String ip, Boolean device);
}
