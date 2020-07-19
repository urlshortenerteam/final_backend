package org.reins.url;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.entity.Users;
import org.reins.url.repository.UsersRepository;
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
        when(usersRepository.findByName("test")).thenReturn(Optional.empty());
        when(usersRepository.save(any(Users.class))).thenReturn(new Users());

        Map<String, String> params = new HashMap<>();
        params.put("name", "test");
        params.put("password", "test");
        params.put("email", "test@sjtu.edu.cn");
        String res = mockMvc.perform(post("/register").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("success"));
    }

    @Test
    public void login() throws Exception {
        Users user1 = new Users();
        Users user2 = new Users();
        user1.setId(2);
        user1.setRole(1);
        user2.setId(3);
        user2.setRole(2);
        when(usersRepository.checkUser("test", "test")).thenReturn(null);
        when(usersRepository.checkUser("test1", "test1")).thenReturn(user1);
        when(usersRepository.checkUser("test2", "test2")).thenReturn(user2);

        Map<String, String> params = new HashMap<>();
        params.put("name", "test");
        params.put("password", "test");
        String res = mockMvc.perform(post("/loginReq").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertFalse(user.getBooleanValue("loginStatus"));
        assertEquals(1, user.getIntValue("type"));
        assertEquals(-1, user.getIntValue("id"));

        params = new HashMap<>();
        params.put("name", "test1");
        params.put("password", "test1");
        res = mockMvc.perform(post("/loginReq").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertTrue(user.getBooleanValue("loginStatus"));
        assertEquals(1, user.getIntValue("type"));
        assertEquals(2, user.getIntValue("id"));

        params = new HashMap<>();
        params.put("name", "test2");
        params.put("password", "test2");
        res = mockMvc.perform(post("/loginReq").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(JSONObject.toJSONString(params)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        user = om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data");
        assertFalse(user.getBooleanValue("loginStatus"));
        assertEquals(2, user.getIntValue("type"));
        assertEquals(3, user.getIntValue("id"));
    }

    @Test
    public void checkSession() throws Exception {
        mockMvc.perform(get("/checkSession").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void banUser() throws Exception {
        Users user0 = new Users();
        Users user1 = new Users();
        Users user2 = new Users();
        user0.setRole(0);
        user1.setRole(1);
        user2.setRole(2);
        when(usersRepository.findById((long) 1)).thenReturn(Optional.of(user0));
        when(usersRepository.findById((long) 2)).thenReturn(Optional.of(user1));
        when(usersRepository.findById((long) 3)).thenReturn(Optional.of(user2));
        when(usersRepository.save(any(Users.class))).thenReturn(new Users());

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

        res = mockMvc.perform(get("/banUser?id=1&ban_id=2&ban=true").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
        }).getJSONObject("data").getBooleanValue("status"));
    }
}
