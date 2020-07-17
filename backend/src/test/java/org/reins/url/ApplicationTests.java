package org.reins.url;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reins.url.entity.Users;
import org.reins.url.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    @Test
    public void contextLoads() {
    }

    @Autowired
    UsersService usersService;

    public void init() {
        usersService.register("test_000000", "test_000000", "test_000000@sjtu.edu.cn");
        usersService.register("test_000001", "test_000001", "test_000001@sjtu.edu.cn");
        usersService.register("test_000002", "test_000002", "test_000002@sjtu.edu.cn");
        Users user = usersService.checkUser("test_000000", "test_000000");
        usersService.changeRole(user.getId(), 0);
        user = usersService.checkUser("test_000002", "test_000002");
        usersService.changeRole(user.getId(), 2);
    }
}
