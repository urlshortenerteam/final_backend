package org.reins.url;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.entity.*;
import org.reins.url.repository.Shorten_logRepository;
import org.reins.url.repository.ShortenerRepository;
import org.reins.url.repository.UsersRepository;
import org.reins.url.repository.Visit_logRepository;
import org.reins.url.service.ShortenerService;
import org.reins.url.service.StatService;
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

    @Autowired
    private StatService statService;
    @Autowired
    private ShortenerService shortenerService;

    @MockBean
    private Shorten_logRepository shorten_logRepository;
    @MockBean
    private ShortenerRepository shortenerRepository;
    @MockBean
    private Visit_logRepository visit_logRepository;
    @MockBean
    private UsersRepository usersRepository;

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
        List<Shorten_log> shorten_logs = new ArrayList<>();
        Shorten_log tmp1 = new Shorten_log();
        tmp1.setId(1);
        tmp1.setCreator_id(1);
        tmp1.setCreate_time(new Date());
        shorten_logs.add(tmp1);
        when(shorten_logRepository.findByCreator_id(1)).thenReturn(shorten_logs);

        List<Shortener> shorteners = new ArrayList<>();
        Shortener tmp2 = new Shortener();
        tmp2.setId("1");
        tmp2.setShort_url("000000");
        tmp2.setShorten_id(1);
        tmp2.setLong_url("https://www.baidu.com");
        shorteners.add(tmp2);
        when(shortenerRepository.findByShorten_id(1)).thenReturn(shorteners);

        List<Visit_log> visit_logs = new ArrayList<>();
        Visit_log tmp3 = new Visit_log();
        tmp3.setId(1);
        tmp3.setShortener_id("1");
        tmp3.setVisit_time(new Date());
        tmp3.setIp("127.0.0.1");
        tmp3.setDevice(true);
        visit_logs.add(tmp3);
        when(visit_logRepository.findByShortener_id("1")).thenReturn(visit_logs);

        String res = mockMvc.perform(get("/getStat?id=1").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<JSONObject> stats = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONArray("data").toJavaList(JSONObject.class);
        assertEquals(stats.size(), 1);
        assertEquals("000000", (stats.get(0).getString("shortUrl")));
        assertEquals((stats.get(0).getLong("count")), 1);
        List<Time_distr> time_distrs = stats.get(0).getJSONArray("time_distr").toJavaList(Time_distr.class);
        assertEquals(time_distrs.size(), 24);
        for (int i = 0; i < 24; ++i) {
            assertEquals(i, time_distrs.get(i).time);
            assertTrue(time_distrs.get(i).value >= 0 && time_distrs.get(i).value <= 1);
        }
        JSONArray longurl = stats.get(0).getJSONArray("longUrl");
        int size = longurl.size();
        assertEquals(size, 1);
        String l = longurl.getJSONObject(0).getString("url");
        assertEquals("https://www.baidu.com", l);


//        String res = mockMvc.perform(get("/getStat?id=1").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        List<JSONObject> stats = om.readValue(res, new TypeReference<JSONObject>() {
//        }).getJSONArray("data").toJavaList(JSONObject.class);
//        for (JSONObject jsonObject : stats) {
//            assertTrue((jsonObject.getString("shortUrl")).matches("[A-Za-z0-9]{6}"));
//            assertTrue((jsonObject.getLong("count")) >= 0);
//            List<Time_distr> time_distrs = jsonObject.getJSONArray("time_distr").toJavaList(Time_distr.class);
//            assertEquals(time_distrs.size(), 24);
//            for (int i = 0; i < 24; ++i) {
//                assertEquals(i, time_distrs.get(i).time);
//                assertTrue(time_distrs.get(i).value >= 0);
//            }
//            JSONArray longurl = jsonObject.getJSONArray("longUrl");
//            int size = longurl.size();
//            for (int i = 0; i < size; ++i) {
//                String l = longurl.getJSONObject(i).getString("url");
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
        tmp2.setShort_url("000000");
        tmp2.setShorten_id(1);
        tmp2.setLong_url("https://www.baidu.com");
        shorteners.add(tmp2);
        when(shortenerRepository.findByShort_url("000000")).thenReturn(shorteners);

        Shorten_log tmp1 = new Shorten_log();
        tmp1.setId(1);
        tmp1.setCreator_id(1);
        tmp1.setCreate_time(new Date());
        when(shorten_logRepository.findById((long) 1)).thenReturn(java.util.Optional.of(tmp1));

        List<Visit_log> visit_logs = new ArrayList<>();
        Visit_log tmp3 = new Visit_log();
        tmp3.setId(1);
        tmp3.setShortener_id("1");
        tmp3.setVisit_time(new Date());
        tmp3.setIp("127.0.0.1");
        tmp3.setDevice(true);
        visit_logs.add(tmp3);
        when(visit_logRepository.findByShortener_id("1")).thenReturn(visit_logs);

        String shortUrl = "000000";

        String res = mockMvc.perform(get("/getShortStat?id=1&short=" + shortUrl).contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject stat = om.readValue(res, new TypeReference<JSONObject>() {
        })
                .getJSONObject("data");
        assertEquals((stat.getString("shortUrl")), "000000");
        assertEquals((stat.getLong("count")), 1);
        List<Time_distr> time_distrs = stat.getJSONArray("time_distr").toJavaList(Time_distr.class);
        assertEquals(time_distrs.size(), 24);
        for (int i = 0; i < 24; ++i) {
            assertEquals(i, time_distrs.get(i).time);
            assertTrue(time_distrs.get(i).value >= 0);
        }
        JSONArray longurl = stat.getJSONArray("longUrl");
        int size = longurl.size();
        assertEquals(size, 1);
        String l = longurl.getJSONObject(0).getString("url");
        assertEquals(l, "https://www.baidu.com");


//        String shortUrl = "B7VAfa";
//
//        String res = mockMvc.perform(get("/getShortStat?id=1&short=" + shortUrl).contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        JSONObject stat = om.readValue(res, new TypeReference<JSONObject>() {
//        })
//                .getJSONObject("data");
//        assertTrue((stat.getString("shortUrl")).matches("[A-Za-z0-9]{6}"));
//        assertTrue((stat.getLong("count")) >= 0);
//        List<Time_distr> time_distrs = stat.getJSONArray("time_distr").toJavaList(Time_distr.class);
//        assertEquals(time_distrs.size(), 24);
//        for (int i = 0; i < 24; ++i) {
//            assertEquals(i, time_distrs.get(i).time);
//            assertTrue(time_distrs.get(i).value >= 0);
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
        tmp.setPassword("");
        tmp.setEmail("123@sjtu.edu.cn");
        tmp.setRole(0);
        tmp.setVisit_count(1);
        usersList.add(tmp);
        when(usersRepository.findAllUserStat()).thenReturn(usersList);

        String res = mockMvc.perform(get("/getUserStat").contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<JSONObject> users = om.readValue(res, new TypeReference<JSONObject>() {
        })
                .getJSONArray("data").toJavaList(JSONObject.class);
        assertEquals(users.size(), 1);
        assertEquals("test", users.get(0).getString("name"));
        assertEquals("", users.get(0).getString("password"));
        assertEquals("123@sjtu.edu.cn", users.get(0).getString("email"));
        assertEquals(users.get(0).getInteger("role"), 0);
        assertEquals(users.get(0).getLong("visit_count"), 1);
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
}
