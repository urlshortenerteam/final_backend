package org.reins.url.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.json.JSONArray;
import org.lionsoul.ip2region.*;

import java.util.*;

@Data
@AllArgsConstructor
public class Statistics {
    public String shortUrl;
    public long count;
    public List<Area_distr> area_distr;
    public Time_distr[] time_distr;
    public List<Source_distr> source_distr;
    public JSONArray longUrl;

    public Statistics() {
        area_distr = new ArrayList<>();
        time_distr = new Time_distr[24];
        for (int i = 0; i < 24; ++i) {
            time_distr[i] = new Time_distr();
            time_distr[i].time = i;
        }
        source_distr = new ArrayList<>();
        count = 0;
        longUrls=new JSONArray();
    }

    public void addArea_distr(String ip) throws Exception {
        boolean isIpAddress = Util.isIpAddress(ip);
        if (!isIpAddress) return;
        //long ipLong=Util.ip2long(ip);
        //String strIp=Util.long2ip(ipLong);
        DbConfig config = new DbConfig();
        String dbfile = "./src/main/resources/ip2region.db";
        DbSearcher searcher = new DbSearcher(config, dbfile);
        DataBlock block = searcher.btreeSearch(ip);
        String area = block.getRegion().substring(6, 8);
        boolean flag = false;
        for (Area_distr area_distr : area_distr) {
            if (area_distr.name.equals(area)) {
                ++area_distr.value;
                flag = true;
                break;
            }
        }
        if (!flag) {
            Area_distr a = new Area_distr();
            a.name = area;
            a.value = 1;
            area_distr.add(a);
        }
    }

    public void addTime_distr(Date visit_time) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(visit_time);
        int hour = calendarTime.get(Calendar.HOUR_OF_DAY);
        time_distr[hour].value++;
    }

    public void addSource_distr(String ip) {
    }
}
