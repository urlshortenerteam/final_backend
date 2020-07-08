package org.reins.url.repository;

import org.reins.url.entity.Shortener;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShortenerReposiy extends MongoRepository<Shortener,Integer> {
}
