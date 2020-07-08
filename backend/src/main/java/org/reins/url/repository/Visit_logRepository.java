package org.reins.url.repository;
import org.reins.url.entity.Visit_log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface Visit_logRepository extends JpaRepository<Visit_log,Long> {
}
