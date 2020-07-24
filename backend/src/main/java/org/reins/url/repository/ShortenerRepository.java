package org.reins.url.repository;

import org.reins.url.entity.Shortener;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "shortener", path = "shortener")
public interface ShortenerRepository extends MongoRepository<Shortener, String> {
    @Query(value = "{'shortenId':?0}")
    List<Shortener> findByShortenId(long shortenId);
}
