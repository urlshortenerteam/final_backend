package org.reins.url.repository;
import org.reins.url.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsersRepository extends JpaRepository<Users,Long> {
}
