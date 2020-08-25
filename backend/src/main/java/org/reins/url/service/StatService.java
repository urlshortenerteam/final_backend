package org.reins.url.service;

import com.alibaba.fastjson.JSONObject;
import org.reins.url.entity.Statistics;
import org.reins.url.entity.Users;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface StatService {
    CompletableFuture<List<Statistics>> getAllUrls();

    CompletableFuture<JSONObject> getNumberCount();

    CompletableFuture<JSONObject> getPagedUrls(Pageable pageable);

    CompletableFuture<Statistics> getShortStat(String shortUrl);

    CompletableFuture<List<Statistics>> getStat(long id);

    CompletableFuture<JSONObject> getStatPageable(long id, Pageable pageable);

    CompletableFuture<List<Users>> getUserStat();
}
