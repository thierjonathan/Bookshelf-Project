package joel.thierry.bookshelf.service;

import joel.thierry.bookshelf.model.Shelf;
import joel.thierry.bookshelf.model.ShelfBook;
import joel.thierry.bookshelf.repository.ShelfBookRepository;
import joel.thierry.bookshelf.repository.ShelfRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import joel.thierry.bookshelf.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final ShelfBookRepository shelfBookRepository;
    private final UserService userService;


    public ShelfService(ShelfRepository shelfRepository, ShelfBookRepository shelfBookRepository, UserService userService) {
        this.shelfRepository = shelfRepository;
        this.shelfBookRepository = shelfBookRepository;
        this.userService = userService;
    }

    public Shelf createShelf(String username, Shelf shelf) {
        // Get userId from username
        String userId = userService.getUserIdByUsername(username);

        // Set userId in Shelf
        shelf.setUserId(userId);

        // Save the shelf in the repository
        return shelfRepository.save(shelf);
    }

    @Transactional
    public void addBookToShelf(String userId, String shelfId, String bookId) {
        Shelf shelf = shelfRepository.findByIdAndUserId(shelfId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Shelf not found for user"));

        if (shelf.isExclusive()) {
            List<Shelf> exclusiveShelves = shelfRepository.findByUserIdAndExclusiveTrue(userId);
            List<String> otherExclusiveShelfIds = exclusiveShelves.stream()
                    .map(Shelf::getId)
                    .filter(id -> !id.equals(shelfId))
                    .collect(Collectors.toList());

            if (!otherExclusiveShelfIds.isEmpty()) {
                shelfBookRepository.deleteByUserIdAndBookIdAndShelfIdIn(userId, bookId, otherExclusiveShelfIds);
            }
        }

        boolean already = shelfBookRepository.existsByUserIdAndBookIdAndShelfId(userId, bookId, shelfId);
        if (!already) {
            ShelfBook sb = new ShelfBook();
            sb.setShelfId(shelfId);
            sb.setBookId(bookId);
            sb.setUserId(userId);
            shelfBookRepository.save(sb);
        }
    }

    public void removeBookFromShelf(String userId, String shelfId, String bookId) {
        List<ShelfBook> current = shelfBookRepository.findByUserIdAndBookId(userId, bookId);
        for (ShelfBook sb : current) {
            if (sb.getShelfId().equals(shelfId)) {
                shelfBookRepository.delete(sb);
                return;
            }
        }
    }
}