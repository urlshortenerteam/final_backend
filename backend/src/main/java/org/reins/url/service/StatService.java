package org.reins.url.service;
import org.reins.url.entity.Statistics;
import java.util.List;
public interface StatService {
    List<Statistics> getStat();
    Statistics getShortStat(long id);
}
