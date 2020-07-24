package org.reins.url.dao;

import org.reins.url.entity.VisitLog;

import java.util.List;

public interface VisitLogDao {
  void addVisitLog(String shortenerId, String ip, Boolean device);

  List<VisitLog> findAllOrderByVisitTime();

  List<VisitLog> findByShortenerId(String shortenerId);
}
