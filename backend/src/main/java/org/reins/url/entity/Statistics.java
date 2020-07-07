package org.reins.url.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {
    public String shortUrl;
    public Long visit_count;
    public List<Area_distr> area_distrs;
    public List<Time_distr> time_distrs;
    public List<Source_distr> source_distrs;
}
