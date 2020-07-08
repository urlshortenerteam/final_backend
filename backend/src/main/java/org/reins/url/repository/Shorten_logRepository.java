package org.reins.url.repository;
import org.reins.url.entity.Shorten_log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface Shorten_logRepository extends JpaRepository<Shorten_log,Long> {
    @Query("select s from Shorten_log s where s.creator_id=:creator_id")
    List<Shorten_log> findByCreator_id(@Param("creator_id")long creator_id);
}
