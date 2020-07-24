package org.reins.url.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.json.JSONArray;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class Statistics {
  public String shortUrl;
  public long count;
  public List<AreaDistr> areaDistr;
  public TimeDistr[] timeDistr;
  public List<SourceDistr> sourceDistr;
  public JSONArray longUrl;
  public String creatorName;
  public Date createTime;

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
    String area = block.getRegion().substring(5, 7);
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
      switch (area){
          case "云南":a.code=530000;break;
          case "黑龙江":a.code=230000;break;
          case "贵州":a.code=520000;break;
          case "北京":a.code=110000;break;
          case "河北":a.code=130000;break;
          case "山西":a.code=140000;break;
          case "吉林":a.code=220000;break;
          case "宁夏":a.code=640000;break;
          case "辽宁":a.code=210000;break;
          case "海南":a.code=460000;break;
          case "内蒙古":a.code=150000;break;
          case "天津":a.code=120000;break;
          case "新疆":a.code=650000;break;
          case "上海":a.code=310000;break;
          case "陕西":a.code=610000;break;
          case "甘肃":a.code=620000;break;
          case "安徽":a.code=340000;break;
          case "香港":a.code=810000;break;
          case "广东":a.code=440000;break;
          case "河南":a.code=410000;break;
          case "湖南":a.code=430000;break;
          case "江西":a.code=360000;break;
          case "四川":a.code=510000;break;
          case "广西":a.code=450000;break;
          case "江苏":a.code=320000;break;
          case "澳门":a.code=820000;break;
          case "浙江":a.code=330000;break;
          case "山东":a.code=370000;break;
          case "青海":a.code=630000;break;
          case "重庆":a.code=500000;break;
          case "福建":a.code=350000;break;
          case "湖北":a.code=420000;break;
          case "西藏":a.code=540000;break;
          case "台湾":a.code=710000;break;
          default:break;
      }
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
