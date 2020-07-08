package org.reins.url.repository;
import org.reins.url.entity.Visit_log;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface Visit_logRepository extends JpaRepository<Visit_log,Long>{
    List<Visit_log> findByShortener_id(long shortener_id);
}
