package org.reins.url.serviceimpl;

import org.reins.url.dao.VisitLogDao;
import org.reins.url.entity.VisitLog;
import org.reins.url.service.VisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class VisitLogServiceImpl implements VisitLogService {
    @Autowired
    private VisitLogDao visitLogDao;

    @Override
    @Async
    public void addVisitLog(String shortenerId, String ip, Boolean device) {
        visitLogDao.addVisitLog(shortenerId, ip, device);
    }

    @Override
    @Async
    public CompletableFuture<List<VisitLog>> findAllOrderByVisitTime() {
        return CompletableFuture.completedFuture(visitLogDao.findAllOrderByVisitTime());
    }
}
