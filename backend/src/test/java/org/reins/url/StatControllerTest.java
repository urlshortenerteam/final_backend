package org.reins.url;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.entity.*;
import org.reins.url.repository.ShortenLogRepository;
import org.reins.url.repository.ShortenerRepository;
import org.reins.url.repository.UsersRepository;
import org.reins.url.repository.VisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatControllerTest extends ApplicationTests {
    @Test
    public void contextLoads() {
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ShortenerRepository shortenerRepository;
    @MockBean
    private ShortenLogRepository shortenLogRepository;
    @MockBean
    private UsersRepository usersRepository;
    @MockBean
    private VisitLogRepository visitLogRepository;

    private final ObjectMapper om = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void getStat() throws Exception {
        List<ShortenLog> shorten_logs = new ArrayList<>();
        ShortenLog tmp1 = new ShortenLog();
        tmp1.setId(1);
        tmp1.setCreatorId(1);
        tmp1.setCreateTime(new Date());
        tmp1.setShortUrl("000000");
        tmp1.setVisitCount(1);
        shorten_logs.add(tmp1);
        when(shortenLogRepository.findByCreatorId(1)).thenReturn(shorten_logs);

        List<Shortener> shorteners = new ArrayList<>();
        Shortener tmp2 = new Shortener();
        tmp2.setId("1");
        tmp2.setShortenId(1);
        tmp2.setLongUrl("https://www.baidu.com/");
        shorteners.add(tmp2);
        when(shortenerRepository.findByShortenId(1)).thenReturn(shorteners);

        List<VisitLog> visit_logs = new ArrayList<>();
        VisitLog tmp3 = new VisitLog();
        tmp3.setId(1);
        tmp3.setShortenerId("1");
        tmp3.setVisitTime(new Date());
        tmp3.setIp("127.0.0.1");
        tmp3.setDevice(true);
        visit_logs.add(tmp3);
        when(visitLogRepository.findByShortenerId("1")).thenReturn(visit_logs);

        String res = mockMvc.perform(get("/getStat?id=1").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<JSONObject> stats = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONArray("data").toJavaList(JSONObject.class);
        assertEquals(stats.size(), 1);
        assertEquals("000000", (stats.get(0).getString("shortUrl")));
        assertEquals((stats.get(0).getLong("count")), 1);
        List<TimeDistr> timeDistrs = stats.get(0).getJSONArray("timeDistr").toJavaList(TimeDistr.class);
        assertEquals(timeDistrs.size(), 24);
        for (int i = 0; i < 24; ++i) {
            assertEquals(i, timeDistrs.get(i).time);
            assertTrue(timeDistrs.get(i).value >= 0 && timeDistrs.get(i).value <= 1);
        }
        JSONArray longUrl = stats.get(0).getJSONArray("longUrl");
        int size = longUrl.size();
        assertEquals(size, 1);
        String l = longUrl.getJSONObject(0).getString("url");
        assertEquals("https://www.baidu.com/", l);


//        String res = mockMvc.perform(get("/getStat?id=1").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        List<JSONObject> stats = om.readValue(res, new TypeReference<JSONObject>() {
//        }).getJSONArray("data").toJavaList(JSONObject.class);
//        for (JSONObject jsonObject : stats) {
//            assertTrue((jsonObject.getString("shortUrl")).matches("[A-Za-z0-9]{6}"));
//            assertTrue((jsonObject.getLong("count")) >= 0);
//            List<Time_distr> timeDistrs = jsonObject.getJSONArray("timeDistr").toJavaList(Time_distr.class);
//            assertEquals(timeDistrs.size(), 24);
//            for (int i = 0; i < 24; ++i) {
//                assertEquals(i, timeDistrs.get(i).time);
//                assertTrue(timeDistrs.get(i).value >= 0);
//            }
//            JSONArray longUrl = jsonObject.getJSONArray("longUrl");
//            int size = longUrl.size();
//            for (int i = 0; i < size; ++i) {
//                String l = longUrl.getJSONObject(i).getString("url");
//                System.out.println(l);
//                assertTrue(l.startsWith("https://") || l.startsWith("http://"));
//            }
//        }
    }

    @Test
    public void getShortStat() throws Exception {
        List<Shortener> shorteners = new ArrayList<>();
        Shortener tmp2 = new Shortener();
        tmp2.setId("1");
        tmp2.setShortenId(1);
        tmp2.setLongUrl("https://www.baidu.com/");
        shorteners.add(tmp2);
        when(shortenerRepository.findByShortenId(1)).thenReturn(shorteners);

        ShortenLog tmp1 = new ShortenLog();
        tmp1.setId(1);
        tmp1.setCreatorId(1);
        tmp1.setShortUrl("000000");
        tmp1.setVisitCount(1);
        tmp1.setCreateTime(new Date());
        when(shortenLogRepository.findByShortUrl("000000")).thenReturn(tmp1);

        List<VisitLog> visit_logs = new ArrayList<>();
        VisitLog tmp3 = new VisitLog();
        tmp3.setId(1);
        tmp3.setShortenerId("1");
        tmp3.setVisitTime(new Date());
        tmp3.setIp("127.0.0.1");
        tmp3.setDevice(true);
        visit_logs.add(tmp3);
        when(visitLogRepository.findByShortenerId("1")).thenReturn(visit_logs);

        String shortUrl = "000000";

        String res = mockMvc.perform(get("/getShortStat?id=1&short=" + shortUrl).contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject stat = om.readValue(res, new TypeReference<JSONObject>() {
        })
                .getJSONObject("data");
        assertEquals((stat.getString("shortUrl")), "000000");
        assertEquals((stat.getLong("count")), 1);
        List<TimeDistr> timeDistrs = stat.getJSONArray("timeDistr").toJavaList(TimeDistr.class);
        assertEquals(timeDistrs.size(), 24);
        for (int i = 0; i < 24; ++i) {
            assertEquals(i, timeDistrs.get(i).time);
            assertTrue(timeDistrs.get(i).value >= 0);
        }
        JSONArray longurl = stat.getJSONArray("longUrl");
        int size = longurl.size();
        assertEquals(size, 1);
        String l = longurl.getJSONObject(0).getString("url");
        assertEquals(l, "https://www.baidu.com/");


        when(shortenLogRepository.findByShortUrl("000001")).thenReturn(null);

        String res2 = mockMvc.perform(get("/getShortStat?id=1&short=000001").contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject stat2 = om.readValue(res2, new TypeReference<JSONObject>() {
        })
                .getJSONObject("data");
        assertEquals((stat2.getLong("count")), -1);


//        String shortUrl = "B7VAfa";
//
//        String res = mockMvc.perform(get("/getShortStat?id=1&short=" + shortUrl).contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        JSONObject stat = om.readValue(res, new TypeReference<JSONObject>() {
//        })
//                .getJSONObject("data");
//        assertTrue((stat.getString("shortUrl")).matches("[A-Za-z0-9]{6}"));
//        assertTrue((stat.getLong("count")) >= 0);
//        List<Time_distr> timeDistrs = stat.getJSONArray("time_distr").toJavaList(Time_distr.class);
//        assertEquals(timeDistrs.size(), 24);
//        for (int i = 0; i < 24; ++i) {
//            assertEquals(i, timeDistrs.get(i).time);
//            assertTrue(timeDistrs.get(i).value >= 0);
//        }
//        JSONArray longurl = stat.getJSONArray("longUrl");
//        int size = longurl.size();
//        for (int i = 0; i < size; ++i) {
//            String l = longurl.getJSONObject(i).getString("url");
//            assertTrue(l.startsWith("https://") || l.startsWith("http://"));
//        }
    }

    @Test
    public void getUserStat() throws Exception {
        List<Users> usersList = new ArrayList<>();
        Users tmp = new Users();
        tmp.setId(1);
        tmp.setName("test");
        tmp.setPassword("test");
        tmp.setEmail("123@sjtu.edu.cn");
        tmp.setRole(0);
        tmp.setVisitCount(1);
        usersList.add(tmp);
        when(usersRepository.findAllUserStat()).thenReturn(usersList);

        String res = mockMvc.perform(get("/getUserStat").contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<JSONObject> users = om.readValue(res, new TypeReference<JSONObject>() {
        })
                .getJSONArray("data").toJavaList(JSONObject.class);
        assertEquals(users.size(), 1);
        assertEquals("test", users.get(0).getString("name"));
        assertNull(users.get(0).getString("password"));
        assertEquals("123@sjtu.edu.cn", users.get(0).getString("email"));
        assertEquals(users.get(0).getInteger("role"), 0);
        assertEquals(users.get(0).getLong("visitCount"), 1);
        assertEquals(users.get(0).getLong("id"), 1);

//        String res = mockMvc.perform(get("/getUserStat").contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        List<JSONObject> users = om.readValue(res, new TypeReference<JSONObject>() {})
//                .getJSONArray("data").toJavaList(JSONObject.class);
//        assertEquals("test0", users.get(0).getString("name"));
//        assertEquals("test0", users.get(0).getString("password"));
//        assertEquals("123@sjtu.edu.cn", users.get(0).getString("email"));
//        for (JSONObject user : users) {
//            assertTrue(user.getLong("id") >= 0);
//            int role = user.getInteger("role");
//            assertTrue(role >= 0 && role <= 2);
//            assertTrue(user.getLong("visit_count") >= 0);
//        }
    }

    @Test
    public void getReal() throws Exception {
        VisitLog visitLog = new VisitLog();
        visitLog.setShortenerId("000000000000000000000000");
        List<VisitLog> visitLogList = new ArrayList<>();
        for (int i = 0; i < 6; i++) visitLogList.add(visitLog);
        when(visitLogRepository.findAllOrderByVisitTime()).thenReturn(visitLogList);
        Shortener shortener = new Shortener();
        shortener.setShortenId(1);
        shortener.setLongUrl("https://www.baidu.com/");
        when(shortenerRepository.findById("000000000000000000000000")).thenReturn(Optional.of(shortener));
        ShortenLog shortenLog = new ShortenLog();
        shortenLog.setCreatorId(1);
        when(shortenLogRepository.findById((long) 1)).thenReturn(Optional.of(shortenLog));

        String res = mockMvc.perform(get("/getReal?id=1").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONArray logs = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getJSONArray("logs");
        assertEquals(5, logs.size());
    }

    @Test
    public void getTopTen() throws Exception {
        ShortenLog shortenLog = new ShortenLog();
        shortenLog.setId(1);
        shortenLog.setShortUrl("000000");
        List<ShortenLog> shortenLogList = new ArrayList<>();
        for (int i = 0; i < 10; i++) shortenLogList.add(shortenLog);
        when(shortenLogRepository.findTop10ByOrderByVisitCountDesc()).thenReturn(shortenLogList);
        Shortener shortener = new Shortener();
        shortener.setLongUrl("https://www.baidu.com/");
        List<Shortener> shortenerList = new ArrayList<>();
        shortenerList.add(shortener);
        when(shortenerRepository.findByShortenId(1)).thenReturn(shortenerList);

        String res = mockMvc.perform(get("/getTopTen").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONArray data = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONArray("data");
        assertEquals(10, data.size());
    }

    @Test
    public void getAllUrls() throws Exception {
        List<ShortenLog> shorten_logs = new ArrayList<>();
        ShortenLog tmp1 = new ShortenLog();
        tmp1.setId(1);
        tmp1.setCreatorId(1);
        tmp1.setCreateTime(new Date());
        tmp1.setShortUrl("000000");
        tmp1.setVisitCount(1);
        shorten_logs.add(tmp1);
        when(shortenLogRepository.findAll()).thenReturn(shorten_logs);

        List<Shortener> shorteners = new ArrayList<>();
        Shortener tmp2 = new Shortener();
        tmp2.setId("1");
        tmp2.setShortenId(1);
        tmp2.setLongUrl("https://www.baidu.com/");
        shorteners.add(tmp2);
        when(shortenerRepository.findByShortenId(1)).thenReturn(shorteners);

        List<VisitLog> visit_logs = new ArrayList<>();
        VisitLog tmp3 = new VisitLog();
        tmp3.setId(1);
        tmp3.setShortenerId("1");
        tmp3.setVisitTime(new Date());
        tmp3.setIp("127.0.0.1");
        tmp3.setDevice(true);
        visit_logs.add(tmp3);
        when(visitLogRepository.findByShortenerId("1")).thenReturn(visit_logs);

        Users tmp4 = new Users();
        tmp4.setName("SXS");
        tmp4.setRole(0);
        tmp4.setVisitCount(1);
        tmp4.setId(1);
        tmp4.setPassword("123456");
        tmp4.setEmail("ao7777@sjtu.edu.cn");
        when(usersRepository.findById((long) 1)).thenReturn(java.util.Optional.of(tmp4));

        String res = mockMvc.perform(get("/getAllUrls").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<JSONObject> stats = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONArray("data").toJavaList(JSONObject.class);
        assertEquals(stats.size(), 1);
        assertEquals("000000", (stats.get(0).getString("shortUrl")));
        assertEquals(stats.get(0).getLong("count"), 1);
        assertEquals(stats.get(0).getString("creatorName"), "SXS");
        List<TimeDistr> timeDistrs = stats.get(0).getJSONArray("timeDistr").toJavaList(TimeDistr.class);
        assertEquals(timeDistrs.size(), 24);
        for (int i = 0; i < 24; ++i) {
            assertEquals(i, timeDistrs.get(i).time);
            assertTrue(timeDistrs.get(i).value >= 0 && timeDistrs.get(i).value <= 1);
        }
        JSONArray longurl = stats.get(0).getJSONArray("longUrl");
        int size = longurl.size();
        assertEquals(size, 1);
        String l = longurl.getJSONObject(0).getString("url");
        assertEquals("https://www.baidu.com/", l);

    }

    @Autowired
    StringEncryptor encryptor;

    @Test
    public void getEncryptor() {
        String url = encryptor.encrypt("jdbc:mysql://reevoo-test-beta.crvfzsr4389e.us-east-1.rds.amazonaws.com:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=CST");
        String name = encryptor.encrypt("admin");
        String password = encryptor.encrypt("reevoo2020");
        String urlMongo = encryptor.encrypt("mongodb://54.165.181.57:27017/url");
        System.out.println(url);
        System.out.println(name);
        System.out.println(password);
        System.out.println(urlMongo);
    }

    @Test
    public void getNumberCount() throws Exception {
        when(usersRepository.count()).thenReturn((long) 1551);
        when(shortenLogRepository.count()).thenReturn((long) 2333);
        when(shortenLogRepository.visitSum()).thenReturn((long) 31415926);

        ShortenLog shortenLog = new ShortenLog();
        shortenLog.setShortUrl("SXSTQL");
        when(shortenLogRepository.findTopByOrderByVisitCountDesc()).thenReturn(shortenLog);

        String res = mockMvc.perform(get("/getNumberCount").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject stats = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertEquals(stats.getLong("userCount"), 1551);
        assertEquals(stats.getLong("shortUrlCount"), 2333);
        assertEquals(stats.getLong("visitCountTotal"), 31415926);
        assertEquals(stats.getString("shortUrl"), "SXSTQL");
    }
}
