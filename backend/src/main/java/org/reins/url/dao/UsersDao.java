package org.reins.url.dao;

import org.reins.url.entity.Users;

import java.util.List;

public interface UsersDao {
    void changeRole(long id, int role);

    void changeVisitCount(long id);

    Users checkUser(String name, String password);

    List<Users> findAllUserStat();

    Users findById(long id);

    boolean doesNameExist(String name);

    void register(String name, String password, String email);
}
