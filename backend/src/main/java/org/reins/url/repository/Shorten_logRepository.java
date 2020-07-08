package org.reins.url.repository;
import org.reins.url.entity.Shorten_log;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface Shorten_logRepository extends JpaRepository<Shorten_log,Long> {
    List<Shorten_log> findByCreator_id(long creator_id);
}
