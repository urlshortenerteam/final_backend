package org.reins.url;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.service.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlControllerTest extends ApplicationTests {
    @Test
    public void contextLoads() {
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

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
    public void generateShort() throws Exception {
        usersInit();

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

        res = mockMvc.perform(post("/getShort?id=2").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrls)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        shortUrls = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONArray("data").toJavaList(String.class);
        assertEquals(2, shortUrls.size());
        assertTrue(shortUrls.get(0).matches("[A-Za-z0-9]{6}"));
        assertTrue(shortUrls.get(1).matches("[A-Za-z0-9]{6}"));
    }

    @Test
    public void generateOneShort() throws Exception {
        usersInit();

        List<String> longUrls = new ArrayList<>();
        longUrls.add("https://www.baidu.com/");
        longUrls.add("https://github.com/");
        String res = mockMvc.perform(post("/getOneShort?id=1").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrls)).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String shortUrl = om.readValue(res, new TypeReference<JSONObject>() {
        }).getString("data");
        assertTrue(shortUrl.matches("[A-Za-z0-9]{6}"));

        res = mockMvc.perform(post("/getOneShort?id=2").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(longUrls)).header("Authorization", "SXSTQL"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        shortUrl = om.readValue(res, new TypeReference<JSONObject>() {
        }).getString("data");
        assertTrue(shortUrl.matches("[A-Za-z0-9]{6}"));
    }

    @Test
    public void getLong() throws Exception {
        usersInit();
        shortenerInit();

        boolean exists = (shortenerService.findByShort_url("000000").size() > 0);
        mockMvc.perform(get("/000000").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(exists ? status().isMovedTemporarily() : status().isOk()).andReturn();

//        String shortUrl = shortenerService.findByShorten_id(1).get(0).getShort_url();
//        mockMvc.perform(get("/" + shortUrl).header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isMovedTemporarily()).andReturn();
    }

    @Test
    public void editUrl() throws Exception {
        usersInit();
        shortenerInit();

        String shortUrl = shortenerService.findByShorten_id(1).get(0).getShort_url();
        String longUrl = "https://www.baidu.com/";
        String res = mockMvc.perform(post("/editUrl?id=1&shortUrl=" + shortUrl).header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(longUrl))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        shortUrl = shortenerService.findByShorten_id(2).get(0).getShort_url();
        res = mockMvc.perform(post("/editUrl?id=0&shortUrl=" + shortUrl).header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(longUrl))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl?id=1&shortUrl=" + shortUrl).header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(longUrl))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

//        res = mockMvc.perform(post("/editUrl?id=2&shortUrl=" + shortUrl).header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(longUrl))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
//        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl?id=1&shortUrl=" + shortUrl).header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content("BANNED"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(post("/editUrl?id=1&shortUrl=" + shortUrl).header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content("LIFT"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));
    }
}
