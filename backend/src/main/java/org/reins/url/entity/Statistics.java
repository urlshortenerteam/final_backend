package org.reins.url.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.json.JSONArray;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
public class Statistics {
    public String shortUrl;
    public long count;
    public AreaDistr[] areaDistr;
    public TimeDistr[] timeDistr;
    public SourceDistr[] sourceDistr;
    public JSONArray longUrl;
    public String creatorName;
    public Date createTime;
    final static int[] codes = {
            530000,
            230000,
            520000,
            110000,
            130000,
            140000,
            220000,
            640000,
            210000,
            460000,
            150000,
            120000,
            650000,
            310000,
            610000,
            620000,
            340000,
            810000,
            440000,
            410000,
            430000,
            360000,
            510000,
            450000,
            320000,
            820000,
            330000,
            370000,
            630000,
            500000,
            350000,
            420000,
            540000,
            710000
    };
    final static String[] names = {
            "云南", "黑龙江", "贵州", "北京", "河北", "山西", "吉林", "宁夏", "辽宁", "海南", "内蒙古", "天津", "新疆", "上海",
            "陕西", "甘肃", "安徽", "香港", "广东", "河南", "湖南", "江西", "四川", "广西", "江苏", "澳门", "浙江", "山东", "青海",
            "重庆", "福建", "湖北", "西藏", "台湾"
    };

    public Statistics() {
        areaDistr = new AreaDistr[34];
        timeDistr = new TimeDistr[24];
        for (int i = 0; i < 24; ++i) {
            timeDistr[i] = new TimeDistr();
            timeDistr[i].time = i;
        }
        for (int i = 0; i < 34; ++i) {
            areaDistr[i] = new AreaDistr();
            areaDistr[i].name = names[i];
            areaDistr[i].code = codes[i];
            areaDistr[i].value = 0;
        }
        sourceDistr = new SourceDistr[2];
        sourceDistr[0] = new SourceDistr();
        sourceDistr[0].source = "电脑";
        sourceDistr[0].value = 0;
        sourceDistr[1] = new SourceDistr();
        sourceDistr[1].source = "手机";
        sourceDistr[1].value = 0;
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
        switch (area) {
            case "云南":
                ++areaDistr[0].value;
                break;
            case "黑龙江":
                ++areaDistr[1].value;
                break;
            case "贵州":
                ++areaDistr[2].value;
                break;
            case "北京":
                ++areaDistr[3].value;
                break;
            case "河北":
                ++areaDistr[4].value;
                break;
            case "山西":
                ++areaDistr[5].value;
                break;
            case "吉林":
                ++areaDistr[6].value;
                break;
            case "宁夏":
                ++areaDistr[7].value;
                break;
            case "辽宁":
                ++areaDistr[8].value;
                break;
            case "海南":
                ++areaDistr[9].value;
                break;
            case "内蒙古":
                ++areaDistr[10].value;
                break;
            case "天津":
                ++areaDistr[11].value;
                break;
            case "新疆":
                ++areaDistr[12].value;
                break;
            case "上海":
                ++areaDistr[13].value;
                break;
            case "陕西":
                ++areaDistr[14].value;
                break;
            case "甘肃":
                ++areaDistr[15].value;
                break;
            case "安徽":
                ++areaDistr[16].value;
                break;
            case "香港":
                ++areaDistr[17].value;
                break;
            case "广东":
                ++areaDistr[18].value;
                break;
            case "河南":
                ++areaDistr[19].value;
                break;
            case "湖南":
                ++areaDistr[20].value;
                break;
            case "江西":
                ++areaDistr[21].value;
                break;
            case "四川":
                ++areaDistr[22].value;
                break;
            case "广西":
                ++areaDistr[23].value;
                break;
            case "江苏":
                ++areaDistr[24].value;
                break;
            case "澳门":
                ++areaDistr[25].value;
                break;
            case "浙江":
                ++areaDistr[26].value;
                break;
            case "山东":
                ++areaDistr[27].value;
                break;
            case "青海":
                ++areaDistr[28].value;
                break;
            case "重庆":
                ++areaDistr[29].value;
                break;
            case "福建":
                ++areaDistr[30].value;
                break;
            case "湖北":
                ++areaDistr[31].value;
                break;
            case "西藏":
                ++areaDistr[32].value;
                break;
            case "台湾":
                ++areaDistr[33].value;
                break;
            default:
                break;
        }
    }

    public void addTimeDistr(Date visitTime) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(visitTime);
        int hour = calendarTime.get(Calendar.HOUR_OF_DAY);
        timeDistr[hour].value++;
    }

    public void addSourceDistr(boolean device) {
        if (device)
            ++sourceDistr[1].value;
        else ++sourceDistr[0].value;
    }
}
