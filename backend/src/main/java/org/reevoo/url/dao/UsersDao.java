package org.reevoo.url.dao;

import org.reevoo.url.entity.Users;

import java.util.List;

public interface UsersDao {
    void changeUser(Users users);

    Users checkUser(String name, String password);

    long count();

    boolean doesNameExist(String name);

    List<Users> findAllUserStat();

    Users findById(long id);

    void register(String name, String password, String email);
}
