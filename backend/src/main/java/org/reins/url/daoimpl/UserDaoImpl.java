package org.reins.url.daoimpl;

import org.reins.url.dao.UserDao;
import org.reins.url.entity.Users;
import org.reins.url.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
@Service
public class UserDaoImpl implements UserDao {
    @Autowired
    UserRepository userRepository;
    @Override
    public List<Users> findAllUserStat(){
        return userRepository.findAllUserStat();
    }
    @Override
    public boolean doesNameExist(String name){
        Optional<Users> user=userRepository.findByName(name);
        return user.isPresent();
    }
    @Override
    public void register(String name,String password){
        Users user=new Users();
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);
    }
}
