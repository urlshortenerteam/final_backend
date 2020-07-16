package org.reins.url.service;

import org.reins.url.entity.Users;

public interface UsersService {
    void changeRole(long id, int role);

    void changeVisit_count(long id);

    Users checkUser(String name, String password);

    Users findById(long id);

    boolean doesNameExist(String name);

    Boolean register(String name, String password, String email);
}