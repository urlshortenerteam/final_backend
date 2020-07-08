package org.reins.url.repository;

import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Visit_log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Shorten_logRepository extends JpaRepository<Shorten_log,Long>{
    @Modifying
    @Query(value="from Shorten_log s where s.creator_id = :Creator_id")
    List<Shorten_log> findByCreator_id(@Param("Creator_id")long Creator_id);

}
