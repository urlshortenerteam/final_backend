package org.reins.url.dao;

import org.reins.url.entity.Shorten_log;
import java.util.List;

public interface StatDao {
    List<Shorten_log> findAll();

}
