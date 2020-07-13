package org.reins.url.serviceimpl;

import org.reins.url.dao.UserDao;
import org.reins.url.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public boolean register(String name,String password){
        if (userDao.doesNameExist(name))
            return false;
        userDao.register(name,password);
        return true;
    }
}
