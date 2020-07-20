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
  private EditLogRepository edit_logRepository;
  @MockBean
  private ShortenerRepository shortenerRepository;
  @MockBean
  private ShortenLogRepository shorten_logRepository;
  @MockBean
  private UsersRepository usersRepository;
  @MockBean
  private VisitLogRepository visit_logRepository;


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
    when(shorten_logRepository.save(any(ShortenLog.class))).thenReturn(new ShortenLog());

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
    when(shorten_logRepository.save(any(ShortenLog.class))).thenReturn(new ShortenLog());

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
    when(shortenerRepository.findByShort_url("000000")).thenReturn(new ArrayList<>());
    Shortener shortener1 = new Shortener();
    shortener1.setShortenId(1);
    shortener1.setLongUrl("https://www.baidu.com/");
    List<Shortener> longUrls = new ArrayList<>();
    longUrls.add(shortener1);
    when(shortenerRepository.findByShort_url("000001")).thenReturn(longUrls);
    ShortenLog shorten_log = new ShortenLog();
    shorten_log.setId(1);
    shorten_log.setCreatorId(1);
    when(shorten_logRepository.findById((long) 1)).thenReturn(Optional.of(shorten_log));
    Shortener shortener2 = new Shortener();
    shortener2.setLongUrl("BANNED");
    List<Shortener> shortenerList = new ArrayList<>();
    shortenerList.add(shortener1);
    shortenerList.add(shortener2);
    when(shortenerRepository.findByShortenId(1)).thenReturn(shortenerList);
    when(usersRepository.findById((long) 1)).thenReturn(Optional.of(new Users()));
    when(visit_logRepository.save(any(VisitLog.class))).thenReturn(new VisitLog());
    when(usersRepository.save(any(Users.class))).thenReturn(new Users());

    mockMvc.perform(get("/000000").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk()).andReturn();

    mockMvc.perform(get("/000001").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isFound()).andReturn();
  }

  @Test
  public void editUrl() throws Exception {
    Users user0 = new Users();
    Users user1 = new Users();
    user0.setRole(0);
    user1.setRole(1);
    when(usersRepository.findById((long) 1)).thenReturn(Optional.of(user0));
    when(usersRepository.findById((long) 2)).thenReturn(Optional.of(user1));
    when(usersRepository.findById((long) 3)).thenReturn(Optional.of(user1));
    when(shortenerRepository.findByShort_url("000000")).thenReturn(new ArrayList<>());
    Shortener shortener1 = new Shortener();
    shortener1.setShortenId(1);
    List<Shortener> shortenerList1 = new ArrayList<>();
    shortenerList1.add(shortener1);
    when(shortenerRepository.findByShort_url("000001")).thenReturn(shortenerList1);
    Shortener shortener2 = new Shortener();
    shortener2.setShortenId(2);
    List<Shortener> shortenerList2 = new ArrayList<>();
    shortenerList2.add(shortener2);
    when(shortenerRepository.findByShort_url("000002")).thenReturn(shortenerList2);
    Shortener shortener3 = new Shortener();
    shortener3.setShortenId(1);
    shortener3.setLongUrl("BANNED");
    List<Shortener> shortenerList3 = new ArrayList<>();
    shortenerList3.add(shortener1);
    shortenerList3.add(shortener3);
    when(shortenerRepository.findByShort_url("000003")).thenReturn(shortenerList3);
    ShortenLog shorten_log = new ShortenLog();
    shorten_log.setId(1);
    shorten_log.setCreatorId(2);
    when(shorten_logRepository.findById((long) 1)).thenReturn(Optional.of(shorten_log));
    when(shortenerRepository.findByShortenId(1)).thenReturn(new ArrayList<>());
    when(shorten_logRepository.findById((long) 2)).thenReturn(Optional.empty());
    when(shortenerRepository.save(any(Shortener.class))).thenReturn(new Shortener());
    when(edit_logRepository.save(any(EditLog.class))).thenReturn(new EditLog());
    when(shortenerRepository.insert(any(Shortener.class))).thenReturn(new Shortener());

    String longUrl = "https://www.baidu.com/";
    String res = mockMvc.perform(post("/editUrl?id=1&shortUrl=000000").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(longUrl))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
    }).getJSONObject("data").getBooleanValue("status"));

    res = mockMvc.perform(post("/editUrl?id=1&shortUrl=000002").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(longUrl))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
    }).getJSONObject("data").getBooleanValue("status"));

    res = mockMvc.perform(post("/editUrl?id=1&shortUrl=000001").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(longUrl))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
    }).getJSONObject("data").getBooleanValue("status"));

    res = mockMvc.perform(post("/editUrl?id=2&shortUrl=000001").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content(longUrl))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
    }).getJSONObject("data").getBooleanValue("status"));

    res = mockMvc.perform(post("/editUrl?id=3&shortUrl=000001").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content("BANNED"))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    assertFalse(om.readValue(res, new TypeReference<JSONObject>() {
    }).getJSONObject("data").getBooleanValue("status"));

    res = mockMvc.perform(post("/editUrl?id=1&shortUrl=000001").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content("BANNED"))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
    }).getJSONObject("data").getBooleanValue("status"));

    res = mockMvc.perform(post("/editUrl?id=1&shortUrl=000003").header("Authorization", "SXSTQL").contentType(MediaType.APPLICATION_JSON_VALUE).content("LIFT"))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    assertTrue(om.readValue(res, new TypeReference<JSONObject>() {
    }).getJSONObject("data").getBooleanValue("status"));
  }
}
