package org.reins.url.serviceimpl;

import org.reins.url.dao.UsersDao;
import org.reins.url.entity.Users;
import org.reins.url.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersDao usersDao;

    @Override
    public void changeVisit_count(long id) {
        usersDao.changeVisit_count(id);
    }

    @Override
    public Users checkUser(String name, String password) {
        return usersDao.checkUser(name, password);
    }

    @Override
    public Users findById(long id) {
        return usersDao.findById(id);
    }

    @Override
    public Boolean register(String name, String password, String email) {
        if (usersDao.doesNameExist(name)) return false;
        usersDao.register(name, password, email);
        return true;
    }
}
