package joel.thierry.bookshelf.repository;

import joel.thierry.bookshelf.model.BookProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookProgressRepository extends MongoRepository<BookProgress, String> {

}
