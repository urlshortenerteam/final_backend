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
    public List<AreaDistr> areaDistr;
    public TimeDistr[] timeDistr;
    public List<SourceDistr> sourceDistr;
    public JSONArray longUrl;

    public Statistics() {
        areaDistr = new ArrayList<>();
        timeDistr = new TimeDistr[24];
        for (int i = 0; i < 24; ++i) {
            timeDistr[i] = new TimeDistr();
            timeDistr[i].time = i;
        }
        sourceDistr = new ArrayList<>();
        count = 0;
        longUrl = new JSONArray();
    }

    public void addAreaDistr(String ip) throws Exception {
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
        for (AreaDistr areaDistr : areaDistr) {
            if (areaDistr.name.equals(area)) {
                areaDistr.value++;
                flag = true;
                break;
            }
        }
        if (!flag) {
            AreaDistr a = new AreaDistr();
            a.name = area;
            a.value = 1;
            areaDistr.add(a);
        }
    }

    public void addTimeDistr(Date visitTime) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(visitTime);
        int hour = calendarTime.get(Calendar.HOUR_OF_DAY);
        timeDistr[hour].value++;
    }

    public void addSourceDistr(String ip) {
    }
}
