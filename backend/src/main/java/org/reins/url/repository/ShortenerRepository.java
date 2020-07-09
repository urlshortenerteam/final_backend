package org.reins.url.repository;
import org.reins.url.entity.Shortener;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
@RepositoryRestResource(collectionResourceRel="shortener",path="shortener")
public interface ShortenerRepository extends MongoRepository<Shortener,String> {
    @Query(value="{'shorten_id':?0}")
    List<Shortener> findByShorten_id(long shorten_id);
    @Query(value="{'short_url':?0}")
    List<Shortener> findByShort_url(String short_url);
}