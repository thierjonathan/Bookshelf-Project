package joel.thierry.bookshelf.repository;

import joel.thierry.bookshelf.model.ShelfBook;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShelfBookRepository extends MongoRepository<ShelfBook, String> {
    List<ShelfBook> findByUserIdAndBookId(String userId, String bookId);
    void deleteByUserIdAndBookIdAndShelfIdIn(String userId, String bookId, List<String> shelfIds);
    boolean existsByUserIdAndBookIdAndShelfId(String userId, String bookId, String shelfId);
    List<ShelfBook> findByShelfIdAndUserId(String shelfId, String userId);
}
