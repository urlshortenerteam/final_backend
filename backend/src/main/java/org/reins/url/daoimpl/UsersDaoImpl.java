package org.reins.url.daoimpl;

import org.reins.url.dao.UsersDao;
import org.reins.url.entity.Users;
import org.reins.url.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
      Users users = usersOptional.get();
      users.setRole(role);
      usersRepository.save(users);
    }
  }

  @Override
  public void changeVisitCount(long id) {
    Optional<Users> usersOptional = usersRepository.findById(id);
    if (usersOptional.isPresent()) {
      Users users = usersOptional.get();
      users.setVisitCount(users.getVisitCount() + 1);
      usersRepository.save(users);
    }
  }

  @Override
  public Users checkUser(String name, String password) {
    return usersRepository.checkUser(name, password);
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
    List<Users> users = usersRepository.findAllUserStat();
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
