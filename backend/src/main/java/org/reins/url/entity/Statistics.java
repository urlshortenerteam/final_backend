package org.reins.url.entity;

import lombok.Data;

import java.util.List;


@Data
public class Statistics {
    String shortUrl;
    Long visit_count;
    List<Area_distr> area_distrs;
    List<Time_distr> time_distrs;
    List<Source_distr> source_distrs;
}
