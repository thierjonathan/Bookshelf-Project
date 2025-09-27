package joel.thierry.bookshelf.repository;

import joel.thierry.bookshelf.model.ShelfBook;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShelfBookRepository extends MongoRepository<ShelfBook, String> {
    Optional<ShelfBook> findByShelfIdAndBookId(String shelfId, String bookId);
}
