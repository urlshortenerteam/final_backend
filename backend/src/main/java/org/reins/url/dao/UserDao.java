package org.reins.url.dao;

import org.reins.url.entity.Users;

import java.util.List;

public interface UserDao {
    List<Users> findAllUserStat();
    boolean doesNameExist(String name);
    void register(String name,String password);
    Users checkUser(String name,String password);
}
