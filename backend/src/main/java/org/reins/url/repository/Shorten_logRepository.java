package org.reins.url.repository;
import org.reins.url.entity.Shorten_log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;
public interface Shorten_logRepository extends JpaRepository<Shorten_log,Long> {
    @Modifying
    @Query(value="insert into Shorten_log(creator_id,create_time) values(:creator_id,:create_time)",nativeQuery=true)
    int addLog(@Param("creator_id")long creator_id,@Param("create_time")Date create_time);
    @Query("select s from Shorten_log s")
    List<Shorten_log> getLog();

    @Modifying
    @Query(value="from Shorten_log s where s.creator_id = :Creator_id")
    List<Shorten_log> findByCreator_id(@Param("Creator_id")long Creator_id);

}
