package joel.thierry.bookshelf.repository;

import joel.thierry.bookshelf.model.Shelf;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShelfRepository extends MongoRepository<Shelf, String> {
    Optional<Shelf> findByIdAndUserId(String id, String userId);
    List<Shelf> findByUserIdAndExclusiveTrue(String userId);
    List<Shelf> findByUserId(String userId);
}
