package org.reins.url.daoimpl;

import org.reins.url.dao.UsersDao;
import org.reins.url.entity.Users;
import org.reins.url.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
@Service
public class UsersDaoImpl implements UsersDao {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void changeRole(long id, int role) {
        Optional<Users> usersOptional = usersRepository.findById(id);
        if (usersOptional.isPresent()) {
            Users users = new Users();
            users.setId(id);
            users.setName(usersOptional.get().getName());
            users.setPassword(usersOptional.get().getPassword());
            users.setEmail(usersOptional.get().getEmail());
            users.setRole(role);
            users.setVisit_count(usersOptional.get().getVisit_count());
            usersRepository.save(users);
        }
    }

    @Override
    public void changeVisit_count(long id) {
        Optional<Users> usersOptional = usersRepository.findById(id);
        if (usersOptional.isPresent()) {
            Users users = new Users();
            users.setId(id);
            users.setName(usersOptional.get().getName());
            users.setPassword(usersOptional.get().getPassword());
            users.setEmail(usersOptional.get().getEmail());
            users.setRole(usersOptional.get().getRole());
            users.setVisit_count(usersOptional.get().getVisit_count() + 1);
            usersRepository.save(users);
        }
    }

    @Override
    public Users checkUser(String name, String password) {
        return usersRepository.checkUser(name, password);
    }

    @Override
    public List<Users> findAllUserStat() {
        return usersRepository.findAllUserStat();
    }

    @Override
    public Users findById(long id) {
        Optional<Users> users = usersRepository.findById(id);
        return users.orElse(null);
    }

    @Override
    public boolean doesNameExist(String name) {
        Optional<Users> user = usersRepository.findByName(name);
        return user.isPresent();
    }

    @Override
    public void register(String name, String password, String email) {
        Users user = new Users();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(1);
        user.setVisit_count(0);
        usersRepository.save(user);
    }
}
