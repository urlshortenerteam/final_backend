package org.reins.url.serviceimpl;

import org.reins.url.dao.VisitLogDao;
import org.reins.url.service.VisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitLogServiceImpl implements VisitLogService {
  @Autowired
  private VisitLogDao visitLogDao;

  @Override
  public void addVisitLog(String shortenerId, String ip, Boolean device) {
    visitLogDao.addVisitLog(shortenerId, ip, device);
  }
}
