package org.reins.url.repository;
import org.reins.url.entity.Shortener;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource(collectionResourceRel="figure",path="figure")
public interface FigureRepository extends MongoRepository<Shortener,Integer> {
}
