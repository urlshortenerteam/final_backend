package org.reins.url.service;
import org.reins.url.entity.Statistics;
import org.reins.url.entity.UserStat;

import java.util.List;
public interface StatService {
    List<Statistics> getStat();
    Statistics getShortStat(String short_url);
    List<UserStat> getUserStat();
}
