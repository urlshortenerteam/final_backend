package org.reins.url;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.entity.Users;
import org.reins.url.service.UsersService;
import org.reins.url.xeger.Xeger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    @Before
    public void setUp() {
        usersService.register("test_000000", "test_000000", "test_000000@sjtu.edu.cn");
        usersService.register("test_000001", "test_000001", "test_000001@sjtu.edu.cn");
        usersService.register("test_000002", "test_000002", "test_000002@sjtu.edu.cn");
        Users user = usersService.checkUser("test_000000", "test_000000");
        usersService.changeRole(user.getId(), 0);
        user = usersService.checkUser("test_000002", "test_000002");
        usersService.changeRole(user.getId(), 2);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void register() throws Exception {
        String name = "test_000001";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("password", name);
        params.put("email", name + "@sjtu.edu.cn");
        String res = mockMvc.perform(post("/register").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("success"));

        Xeger xeger = new Xeger("[A-Za-z0-9]{6}", new Random(0));
        name = "test_" + xeger.generate();
        boolean exists = usersService.doesNameExist(name);
        params = new HashMap<>();
        params.put("name", name);
        params.put("password", name);
        params.put("email", name + "@sjtu.edu.cn");
        res = mockMvc.perform(post("/register").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotEquals(exists, om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("success"));
    }

    @Test
    public void login() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", "test_000001");
        params.put("password", "wrong");
        String res = mockMvc.perform(post("/loginReq").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertFalse(user.getBooleanValue("loginStatus"));
        assertEquals(1, user.getIntValue("type"));
        assertEquals(-1, user.getIntValue("id"));

        params = new HashMap<>();
        params.put("name", "test_000001");
        params.put("password", "test_000001");
        res = mockMvc.perform(post("/loginReq").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertTrue(user.getBooleanValue("loginStatus"));
        assertEquals(1, user.getIntValue("type"));
        assertEquals(2, user.getIntValue("id"));

        params = new HashMap<>();
        params.put("name", "test_000002");
        params.put("password", "test_000002");
        res = mockMvc.perform(post("/loginReq").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertFalse(user.getBooleanValue("loginStatus"));
        assertEquals(2, user.getIntValue("type"));
        assertEquals(3, user.getIntValue("id"));
    }

    @Test
    public void banUser() throws Exception {
        String res = mockMvc.perform(get("/banUser?id=0&ban_id=0&ban=true").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(get("/banUser?id=1&ban_id=1&ban=true").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(get("/banUser?id=2&ban_id=2&ban=true").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(get("/banUser?id=1&ban_id=2&ban=false").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));
    }
}
