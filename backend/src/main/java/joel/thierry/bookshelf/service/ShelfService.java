package joel.thierry.bookshelf.service;

import joel.thierry.bookshelf.model.Shelf;
import joel.thierry.bookshelf.repository.ShelfRepository;

public class ShelfService {
    private final ShelfRepository shelfRepository;

    public ShelfService(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }
    public void createShelf(Shelf shelf){
        shelfRepository.save(shelf);
    }
}
