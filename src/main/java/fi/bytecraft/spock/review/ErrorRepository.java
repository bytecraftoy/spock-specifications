package fi.bytecraft.spock.review;

import fi.bytecraft.spock.review.models.Error;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends CrudRepository<Error, Long> {

}
