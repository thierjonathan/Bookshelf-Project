package joel.thierry.bookshelf.repository;

import joel.thierry.bookshelf.model.BookProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookProgressRepository extends MongoRepository<BookProgress, String> {
    Optional<BookProgress> findByUserIdAndBookId(String userId, String bookId);
}
