package org.reevoo.url.service;

import org.reevoo.url.entity.Users;

import java.util.concurrent.CompletableFuture;

public interface UsersService {
    void changeRole(long id, int role);

    CompletableFuture<Users> checkUser(String name, String password);

    CompletableFuture<Users> findById(long id);

    CompletableFuture<Boolean> register(String name, String password, String email);
}
