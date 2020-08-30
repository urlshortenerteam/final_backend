package org.reevoo.url.serviceimpl;

import org.reevoo.url.service.VisitLogService;
import org.reevoo.url.dao.VisitLogDao;
import org.reevoo.url.entity.VisitLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    public CompletableFuture<List<VisitLog>> findTop5ByShortenerIdOrderByVisitTimeDesc(List<String> shorteners) {
        return CompletableFuture.completedFuture(visitLogDao.findTop5ByShortenerIdOrderByVisitTimeDesc(shorteners));
    }
}
