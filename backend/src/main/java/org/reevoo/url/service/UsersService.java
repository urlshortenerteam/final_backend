package org.reevoo.url.service;

import org.reevoo.url.entity.Users;

import java.util.concurrent.CompletableFuture;

public interface UsersService {
    void changeUser(Users users);

    CompletableFuture<Users> checkUser(String name, String password);

    CompletableFuture<Users> findById(long id);

    CompletableFuture<Boolean> register(String name, String password, String email);
}
