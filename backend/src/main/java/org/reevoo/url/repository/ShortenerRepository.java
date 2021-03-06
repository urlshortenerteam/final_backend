package org.reevoo.url.repository;

import org.reevoo.url.entity.Shortener;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "shortener", path = "shortener")
public interface ShortenerRepository extends MongoRepository<Shortener, String> {
    List<Shortener> findByShortenId(long shortenId);

    List<Shortener> findByShortenIdIn(List<Long> shortenId);
}
