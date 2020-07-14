package org.reins.url.serviceimpl;

import org.reins.url.dao.UserDao;
import org.reins.url.entity.Users;
import org.reins.url.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public boolean register(String name, String password) {
        if (userDao.doesNameExist(name))
            return false;
        userDao.register(name, password);
        return true;
    }

    @Override
    public Users checkUser(String name, String password) {
        return userDao.checkUser(name, password);
    }
}
