package org.reevoo.url.daoimpl;

import org.jasypt.encryption.StringEncryptor;
import org.reevoo.url.dao.UsersDao;
import org.reevoo.url.entity.Users;
import org.reevoo.url.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
@Service
public class UsersDaoImpl implements UsersDao {
    @Autowired
    private StringEncryptor stringEncryptor;
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void changeUser(Users users) {
        usersRepository.save(users);
    }

    @Override
    public Users checkUser(String name, String password) {
        Users user = usersRepository.findByName(name);
        if (user != null && stringEncryptor.decrypt(user.getPassword()).equals(password)) return user;
        return null;
    }

    @Override
    public long count() {
        return usersRepository.count();
    }

    @Override
    public boolean doesNameExist(String name) {
        return usersRepository.findByName(name) != null;
    }

    @Override
    public List<Users> findAllUserStat() {
        List<Users> users = usersRepository.findAll();
        List<Users> res = new ArrayList<>();
        for (Users user : users) {
            Users tmp = new Users();
            tmp.setId(user.getId());
            tmp.setName(user.getName());
            tmp.setEmail(user.getEmail());
            tmp.setRole(user.getRole());
            tmp.setVisitCount(user.getVisitCount());
            res.add(tmp);
        }
        return res;
    }

    @Override
    public Users findById(long id) {
        return usersRepository.findById(id).orElse(null);
    }

    @Override
    public void register(String name, String password, String email) {
        Users user = new Users();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(1);
        user.setVisitCount(0);
        usersRepository.save(user);
    }
}
