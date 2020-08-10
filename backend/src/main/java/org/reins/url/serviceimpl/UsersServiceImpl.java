package org.reins.url.serviceimpl;

import org.jasypt.encryption.StringEncryptor;
import org.reins.url.dao.UsersDao;
import org.reins.url.entity.Users;
import org.reins.url.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    StringEncryptor stringEncryptor;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    StringEncryptor encryptor;

    @Override
    @Async
    public void changeRole(long id, int role) {
        usersDao.changeRole(id, role);
    }

    @Override
    @Async
    public void changeVisitCount(long id) {
        usersDao.changeVisitCount(id);
    }

    @Override
    @Async
    public CompletableFuture<Users> checkUser(String name, String password) {
        return CompletableFuture.completedFuture(usersDao.checkUser(name, password));
    }

    @Override
    @Async
    public CompletableFuture<Users> findById(long id) {
        return CompletableFuture.completedFuture(usersDao.findById(id));
    }

    @Override
    @Async
    public CompletableFuture<Boolean> register(String name, String password, String email) {
        if (usersDao.doesNameExist(name)) return CompletableFuture.completedFuture(false);
        usersDao.register(name, stringEncryptor.encrypt(password), email);
        return CompletableFuture.completedFuture(true);
    }
}
