package org.reins.url;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.entity.Users;
import org.reins.url.repository.UsersRepository;
import org.reins.url.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
    private StringEncryptor stringEncryptor;

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
    public void register() throws Exception {
        when(usersRepository.findByName("test")).thenReturn(null);
        when(usersRepository.save(any(Users.class))).thenReturn(new Users());

        Map<String, String> params = new HashMap<>();
        params.put("name", "test");
        params.put("password", "test");
        params.put("email", "test@sjtu.edu.cn");
        String res = mockMvc.perform(post("/register").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("success"));
    }

    @Test
    public void login() throws Exception {
        Users user1 = new Users();
        Users user2 = new Users();
        user1.setId(1);
        user1.setPassword(stringEncryptor.encrypt("test1"));
        user1.setRole(1);
        user2.setId(2);
        user2.setPassword(stringEncryptor.encrypt("test2"));
        user2.setRole(2);
        when(usersRepository.findByName("test")).thenReturn(null);
        when(usersRepository.findByName("test1")).thenReturn(user1);
        when(usersRepository.findByName("test2")).thenReturn(user2);

        Map<String, String> params = new HashMap<>();
        params.put("name", "test");
        params.put("password", "test");
        String res = mockMvc.perform(post("/loginReq").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertFalse(user.getBooleanValue("loginStatus"));
        assertEquals(1, user.getIntValue("type"));
        assertEquals(-1, user.getIntValue("id"));

        params = new HashMap<>();
        params.put("name", "test1");
        params.put("password", "test1");
        res = mockMvc.perform(post("/loginReq").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertTrue(user.getBooleanValue("loginStatus"));
        assertEquals(1, user.getIntValue("type"));
        assertEquals(1, user.getIntValue("id"));

        params = new HashMap<>();
        params.put("name", "test2");
        params.put("password", "test2");
        res = mockMvc.perform(post("/loginReq").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertFalse(user.getBooleanValue("loginStatus"));
        assertEquals(2, user.getIntValue("type"));
        assertEquals(2, user.getIntValue("id"));
    }

    @Test
    public void checkSession() throws Exception {
        mockMvc.perform(get("/checkSession").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void banUser() throws Exception {
        Users user = new Users();
        user.setRole(1);
        when(usersRepository.findById((long) 0)).thenReturn(Optional.empty());
        when(usersRepository.findById((long) 2)).thenReturn(Optional.of(user));
        when(usersRepository.save(any(Users.class))).thenReturn(new Users());

        String res = mockMvc.perform(get("/banUser?banId=1&ban=true").header("Authorization", JwtUtil.sign(2, "ao7777", 1, false)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getBooleanValue("not_administrator"));

        res = mockMvc.perform(get("/banUser?banId=0&ban=true").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getBooleanValue("not_administrator"));
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));

        res = mockMvc.perform(get("/banUser?banId=2&ban=true").header("Authorization", JwtUtil.sign(1, "ao7777", 0, false)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
        }).getBooleanValue("not_administrator"));
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));
    }

    @Test
    public void refresh() throws Exception {
        Users users = new Users();
        users.setId(1);
        users.setName("ao7777");
        users.setRole(0);
        when(usersRepository.findById((long) 1)).thenReturn(Optional.of(users));

        Map<String, String> params = new HashMap<>();
        params.put("refresh", JwtUtil.sign(1, "ao7777", 0, true));
        String res = mockMvc.perform(post("/refresh").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        boolean success = om.readValue(res, new TypeReference<JSONObject>() {
        }).getBoolean("success");
        assertTrue(success);

        users.setRole(2);
        when(usersRepository.findById((long) 1)).thenReturn(Optional.of(users));
        params = new HashMap<>();
        params.put("refresh", JwtUtil.sign(1, "ao7777", 1, true));
        res = mockMvc.perform(post("/refresh").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        success = om.readValue(res, new TypeReference<JSONObject>() {
        }).getBoolean("success");
        assertFalse(success);

        params = new HashMap<>();
        params.put("refresh", "SXSTXDY");
        res = mockMvc.perform(post("/refresh").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        success = om.readValue(res, new TypeReference<JSONObject>() {
        }).getBoolean("success");
        assertFalse(success);
    }

    @Test
    public void refresh() throws Exception {
        Users users=new Users();
        users.setId(1);
        users.setName("ao7777");
        users.setRole(0);
        when(usersRepository.findById((long) 1)).thenReturn(Optional.of(users));

        Map<String, String> params = new HashMap<>();
        params.put("refresh", JwtUtil.sign(1,"ao7777",0,true));
        String res = mockMvc.perform(post("/refresh").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        boolean success = om.readValue(res, new TypeReference<JSONObject>() {
        }).getBoolean("success");
        assertTrue(success);

        users.setRole(2);
        when(usersRepository.findById((long) 1)).thenReturn(Optional.of(users));
        params = new HashMap<>();
        params.put("refresh", JwtUtil.sign(1,"ao7777",1,true));
        res = mockMvc.perform(post("/refresh").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        success = om.readValue(res, new TypeReference<JSONObject>() {
        }).getBoolean("success");
        assertFalse(success);

        params = new HashMap<>();
        params.put("refresh", "SXSTXDY");
        res = mockMvc.perform(post("/refresh").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        success = om.readValue(res, new TypeReference<JSONObject>() {
        }).getBoolean("success");
        assertFalse(success);
    }
}
