package org.reins.url;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.entity.*;
import org.reins.url.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        String res = mockMvc.perform(post("/getShort?id=1").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrls)))
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
        String res = mockMvc.perform(post("/getOneShort?id=1").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrls)).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String shortUrl = om.readValue(res, new TypeReference<JSONObject>() {
        }).getString("data");
        assertTrue(shortUrl.matches("[A-Za-z0-9]{6}"));
    }

    @Test
    public void getLong() throws Exception {
        ShortenLog shortenLog = new ShortenLog();
        shortenLog.setId(1);
        shortenLog.setCreatorId(1);
        when(shortenLogRepository.findByShortUrl("000000")).thenReturn(shortenLog);
        Shortener shortener = new Shortener();
        shortener.setLongUrl("https://www.baidu.com/");
        List<Shortener> shortenerList = new ArrayList<>();
        shortenerList.add(shortener);
        when(shortenerRepository.findByShortenId(1)).thenReturn(shortenerList);
        when(shortenLogRepository.save(any(ShortenLog.class))).thenReturn(new ShortenLog());
        when(usersRepository.findById((long) 1)).thenReturn(Optional.of(new Users()));
        when(usersRepository.save(any(Users.class))).thenReturn(new Users());
        when(visitLogRepository.save(any(VisitLog.class))).thenReturn(new VisitLog());

        mockMvc.perform(get("/000000").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isFound()).andReturn();
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

        String res = mockMvc.perform(post("/editUrl?id=0&shortUrl=000000").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrl)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl?id=1&shortUrl=000001").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrl)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl?id=3&shortUrl=000000").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrl)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl?id=2&shortUrl=000000").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrl)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl?id=1&shortUrl=000000").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString("LIFT")))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl?id=1&shortUrl=000000").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString("BANNED")))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));
    }
}
