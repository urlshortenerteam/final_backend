package org.reins.url.repository;

import org.reins.url.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query("from Users where name=:name and password=:password")
    Users checkUser(@Param("name") String name, @Param("password") String password);

    @Query("select u from Users u")
    List<Users> findAllUserStat();

    @Query("select s.id from Users s where s.name=:name")
    Users findByName(@Param("name") String name);
}
