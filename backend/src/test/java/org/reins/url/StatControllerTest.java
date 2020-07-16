package org.reins.url;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.entity.Time_distr;
import org.reins.url.service.ShortenerService;
import org.reins.url.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    private ObjectMapper om = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void getStat() throws Exception {
        String res = mockMvc.perform(get("/getStat?id=1").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<JSONObject> stats = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONArray("data").toJavaList(JSONObject.class);
        for (JSONObject jsonObject : stats) {
            assertTrue((jsonObject.getString("shortUrl")).matches("[A-Za-z0-9]{6}"));
            assertTrue((jsonObject.getLong("count")) >= 0);
            List<Time_distr> time_distrs = jsonObject.getJSONArray("time_distr").toJavaList(Time_distr.class);
            assertEquals(time_distrs.size(), 24);
            for (int i = 0; i < 24; ++i) {
                assertEquals(i, time_distrs.get(i).time);
                assertTrue(time_distrs.get(i).value >= 0);
            }
            JSONArray longurl = jsonObject.getJSONArray("longUrl");
            int size = longurl.size();
            for (int i = 0; i < size; ++i) {
                String l = longurl.getJSONObject(i).getString("url");
                assertTrue(l.startsWith("https://") || l.startsWith("http://"));
            }
        }
    }

    @Test
    public void getShortStat() throws Exception {
        String shortUrl = "B7VAfa";
        String res = mockMvc.perform(get("/getShortStat?id=1&short=" + shortUrl).contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject stat = om.readValue(res, new TypeReference<JSONObject>() {
        })
                .getJSONObject("data");
        assertTrue((stat.getString("shortUrl")).matches("[A-Za-z0-9]{6}"));
        assertTrue((stat.getLong("count")) >= 0);
        List<Time_distr> time_distrs = stat.getJSONArray("time_distr").toJavaList(Time_distr.class);
        assertEquals(time_distrs.size(), 24);
        for (int i = 0; i < 24; ++i) {
            assertEquals(i, time_distrs.get(i).time);
            assertTrue(time_distrs.get(i).value >= 0);
        }
        JSONArray longurl = stat.getJSONArray("longUrl");
        int size = longurl.size();
        assertEquals(size, shortenerService.findByShort_url(shortUrl).size());
        for (int i = 0; i < size; ++i) {
            String l = longurl.getJSONObject(i).getString("url");
            assertTrue(l.startsWith("https://") || l.startsWith("http://"));
        }
    }

    @Test
    public void getUserStat() throws Exception {
        String res = mockMvc.perform(get("/getUserStat").contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<JSONObject> users = om.readValue(res, new TypeReference<JSONObject>() {
        })
                .getJSONArray("data").toJavaList(JSONObject.class);
        assertEquals("test0", users.get(0).getString("name"));
        assertEquals("test0", users.get(0).getString("password"));
        assertEquals("test0@sjtu.edu.cn", users.get(0).getString("email"));
        for (JSONObject user : users) {
            assertTrue(user.getLong("id") >= 0);
            int role = user.getInteger("role");
            assertTrue(role >= 0 && role <= 2);
            assertTrue(user.getLong("visit_count") >= 0);
        }
    }
}
