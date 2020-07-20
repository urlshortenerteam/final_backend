package org.reins.url.service;

import org.reins.url.entity.VisitLog;

import java.util.List;

public interface VisitLogService {
  void addVisitLog(String shortenerId, String ip, Boolean device);

  List<VisitLog> findAllOrderByVisit_time();
}
