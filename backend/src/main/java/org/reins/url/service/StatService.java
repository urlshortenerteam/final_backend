package org.reins.url.service;

import com.alibaba.fastjson.JSONObject;
import org.reins.url.entity.Statistics;
import org.reins.url.entity.Users;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StatService {
    List<Statistics> getAllUrls();

    JSONObject getNumberCount();

    JSONObject getPagedUrls(Pageable pageable);

    Statistics getShortStat(String shortUrl);

    List<Statistics> getStat(long id);

    JSONObject getStatPageable(long id,Pageable pageable);

    List<Users> getUserStat();
}
