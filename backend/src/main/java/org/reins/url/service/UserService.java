package org.reins.url.service;

import org.reins.url.entity.Users;

public interface UserService {
    boolean register(String name,String password);
    Users checkUser(String name, String password);
}

