package joel.thierry.bookshelf.repository;

import joel.thierry.bookshelf.model.Shelf;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShelfRepository extends MongoRepository<Shelf, String> {

}
