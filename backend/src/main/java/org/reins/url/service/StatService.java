package org.reins.url.service;

import org.reins.url.entity.Statistics;
import org.reins.url.entity.Users;

import java.util.List;

public interface StatService {
    List<Statistics> getStat(long id);

    Statistics getShortStat(String shortUrl);

    List<Users> getUserStat();
}
