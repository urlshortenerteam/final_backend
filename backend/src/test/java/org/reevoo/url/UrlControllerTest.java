package org.reevoo.url;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reevoo.url.entity.EditLog;
import org.reevoo.url.entity.ShortenLog;
import org.reevoo.url.entity.Shortener;
import org.reevoo.url.entity.Users;
import org.reevoo.url.repository.*;
import org.reevoo.url.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlControllerTest extends ApplicationTests {
    private final ObjectMapper om = new ObjectMapper();

    @Test
    public void contextLoads() {
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private EditLogRepository editLogRepository;
    @MockBean
    private ShortenerRepository shortenerRepository;
    @MockBean
    private ShortenLogRepository shortenLogRepository;
    @MockBean
    private UsersRepository usersRepository;
    @MockBean
    private VisitLogRepository visitLogRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void generateShort() throws Exception {
        when(shortenerRepository.insert(any(Shortener.class))).thenReturn(new Shortener());
        when(shortenLogRepository.save(any(ShortenLog.class))).thenReturn(new ShortenLog());

        List<String> longUrls = new ArrayList<>();
        longUrls.add("https://www.baidu.com/");
        longUrls.add("https://github.com/");
        String res = mockMvc.perform(post("/getShort").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrls)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<String> shortUrls = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONArray("data").toJavaList(String.class);
        assertEquals(2, shortUrls.size());
        assertTrue(shortUrls.get(0).matches("[A-Za-z0-9]{6}"));
        assertTrue(shortUrls.get(1).matches("[A-Za-z0-9]{6}"));
    }

    @Test
    public void generateOneShort() throws Exception {
        when(shortenerRepository.insert(any(Shortener.class))).thenReturn(new Shortener());
        when(shortenLogRepository.save(any(ShortenLog.class))).thenReturn(new ShortenLog());

        List<String> longUrls = new ArrayList<>();
        longUrls.add("https://www.baidu.com/");
        longUrls.add("https://github.com/");
        String res = mockMvc.perform(post("/getOneShort").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrls)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String shortUrl = om.readValue(res, new TypeReference<JSONObject>() {
        }).getString("data");
        assertTrue(shortUrl.matches("[A-Za-z0-9]{6}"));
    }

    @Test
    public void editUrl() throws Exception {
        String longUrl = "https://www.baidu.com/";
        Users user1 = new Users();
        Users user2 = new Users();
        user1.setRole(0);
        user2.setRole(1);
        when(usersRepository.findById((long) 0)).thenReturn(Optional.empty());
        when(usersRepository.findById((long) 1)).thenReturn(Optional.of(user1));
        when(usersRepository.findById((long) 2)).thenReturn(Optional.of(user2));
        when(usersRepository.findById((long) 3)).thenReturn(Optional.of(user2));
        ShortenLog shortenLog1 = new ShortenLog();
        ShortenLog shortenLog2 = new ShortenLog();
        shortenLog1.setId(1);
        shortenLog1.setCreatorId(2);
        shortenLog2.setId(2);
        when(shortenLogRepository.findByShortUrl("000000")).thenReturn(shortenLog1);
        when(shortenLogRepository.findByShortUrl("000001")).thenReturn(shortenLog2);
        Shortener shortener1 = new Shortener();
        Shortener shortener2 = new Shortener();
        Shortener shortener3 = new Shortener();
        shortener1.setLongUrl(longUrl);
        shortener2.setLongUrl("BANNED");
        shortener3.setLongUrl("LIFT");
        List<Shortener> shortenerList = new ArrayList<>();
        shortenerList.add(shortener1);
        shortenerList.add(shortener2);
        shortenerList.add(shortener3);
        when(shortenerRepository.findByShortenId(1)).thenReturn(shortenerList);
        when(shortenerRepository.findByShortenId(2)).thenReturn(new ArrayList<>());
        when(editLogRepository.save(any(EditLog.class))).thenReturn(new EditLog());
        when(shortenerRepository.insert(any(Shortener.class))).thenReturn(new Shortener());

        Map<String, String> params1 = new HashMap<>();
        Map<String, String> params2 = new HashMap<>();
        Map<String, String> params3 = new HashMap<>();
        Map<String, String> params4 = new HashMap<>();
        params1.put("shortUrl", "000000");
        params1.put("longUrl", longUrl);
        params2.put("shortUrl", "000000");
        params2.put("longUrl", "BANNED");
        params3.put("shortUrl", "000000");
        params3.put("longUrl", "LIFT");
        params4.put("shortUrl", "000001");
        params4.put("longUrl", longUrl);

        String res = mockMvc.perform(post("/editUrl").header("Authorization", JwtUtil.sign(0, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params1)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params4)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl").header("Authorization", JwtUtil.sign(3, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params1)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl").header("Authorization", JwtUtil.sign(2, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params1)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params3)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params2)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));
    }
}
