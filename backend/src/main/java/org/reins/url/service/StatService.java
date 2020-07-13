package org.reins.url.service;
import org.reins.url.entity.Statistics;
import org.reins.url.entity.UserStat;
import org.reins.url.entity.Users;

import java.util.List;
public interface StatService {
    List<Statistics> getStat();
    Statistics getShortStat(String short_url);
    List<Users> getUserStat();
}
