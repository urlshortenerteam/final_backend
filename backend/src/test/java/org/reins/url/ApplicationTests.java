package org.reins.url;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.controller.UrlController;
import org.reins.url.entity.Users;
import org.reins.url.service.Shorten_logService;
import org.reins.url.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    @Test
    public void contextLoads() {
    }

    @Autowired
    Shorten_logService shorten_logService;

    @Autowired
    UsersService usersService;

    @Autowired
    UrlController urlController;

    public void shortenerInit() {
        List<String> longUrls = new ArrayList<>();
        List<String> shortUrls = new ArrayList<>();
        longUrls.add("https://www.baidu.com/");
        longUrls.add("https://github.com/");
        String shortUrl = urlController.long2short(longUrls.get((int) (Math.random() * longUrls.size())));
        shortUrls.add(shortUrl);
        shortUrls.add(shortUrl);
        shorten_logService.addShorten_log(2, shortUrls, longUrls);
        shortUrls = new ArrayList<>();
        shortUrls.add(urlController.long2short(longUrls.get(0)));
        shortUrls.add(urlController.long2short(longUrls.get(1)));
        shorten_logService.addShorten_log(2, shortUrls, longUrls);
    }

    public void usersInit() {
        usersService.register("test_000000", "test_000000", "test_000000@sjtu.edu.cn");
        usersService.register("test_000001", "test_000001", "test_000001@sjtu.edu.cn");
        usersService.register("test_000002", "test_000002", "test_000002@sjtu.edu.cn");
        Users user = usersService.checkUser("test_000000", "test_000000");
        usersService.changeRole(user.getId(), 0);
        user = usersService.checkUser("test_000002", "test_000002");
        usersService.changeRole(user.getId(), 2);
    }
}
