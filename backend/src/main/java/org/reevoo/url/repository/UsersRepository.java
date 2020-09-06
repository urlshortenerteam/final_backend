package org.reevoo.url.repository;

import org.reevoo.url.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {
    //@Query("select u from Users u")
    List<Users> findAll();

    @Query("select u from Users u where u.name=:name")
    Users findByName(@Param("name") String name);
}
