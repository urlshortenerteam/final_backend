package org.reins.url.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
public class Statistics {
    public String shortUrl;
    public long visit_count;
    public List<Area_distr> area_distrs;
    public Time_distr[] time_distrs;
    public List<Source_distr> source_distrs;

    public Statistics(){
        area_distrs=new ArrayList<>();
        time_distrs=new Time_distr[24];
        for (int i=0;i<24;++i)
            time_distrs[i].time=i;
        source_distrs=new ArrayList<>();
        visit_count=0;
    }

    public void addArea_distr(String ip){

    }

    public void addTime_distr(Date visit_time){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(visit_time);
        int hour=calendarTime.get(Calendar.HOUR_OF_DAY);
        time_distrs[hour].value++;
    }

    public void addSource_distr(String ip){

    }
}
