package org.reins.url;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersControllerTest extends ApplicationTests {
    @Test
    public void contextLoads() {
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UsersService usersService;

    private ObjectMapper om = new ObjectMapper();

    private String testName() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * chars.length());
            name.append(chars, index, index + 1);
        }
        return "test_" + name.toString();
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void register() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", "test0");
        params.put("password", "test0");
        params.put("email", "test0@sjtu.edu.cn");
        String res = mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("success"));

        String name = testName();
        params = new HashMap<>();
        params.put("name", name);
        params.put("password", "123456");
        params.put("email", name + "@sjtu.edu.cn");
        boolean exists = usersService.doesNameExist(name);
        res = mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotEquals(exists, om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("success"));
    }

    @Test
    public void login() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", "test0");
        params.put("password", "wrong");
        String res = mockMvc.perform(post("/loginReq").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertFalse(user.getBooleanValue("loginStatus"));
        assertEquals(1, user.getIntValue("type"));
        assertEquals(-1, user.getIntValue("id"));

        params = new HashMap<>();
        params.put("name", "test1");
        params.put("password", "test1");
        res = mockMvc.perform(post("/loginReq").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertTrue(user.getBooleanValue("loginStatus"));
        assertEquals(1, user.getIntValue("type"));
        assertEquals(2, user.getIntValue("id"));

        params = new HashMap<>();
        params.put("name", "test2");
        params.put("password", "test2");
        res = mockMvc.perform(post("/loginReq").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertFalse(user.getBooleanValue("loginStatus"));
        assertEquals(2, user.getIntValue("type"));
        assertEquals(3, user.getIntValue("id"));
    }

    @Test
    public void banUser() throws Exception {
        String res = mockMvc.perform(get("/banUser?id=0&ban_id=0&ban=true").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(get("/banUser?id=1&ban_id=1&ban=true").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(get("/banUser?id=2&ban_id=2&ban=true").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(get("/banUser?id=1&ban_id=2&ban=false").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));
    }
}
