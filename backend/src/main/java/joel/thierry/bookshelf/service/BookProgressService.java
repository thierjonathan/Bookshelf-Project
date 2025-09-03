package joel.thierry.bookshelf.service;

import joel.thierry.bookshelf.dto.BookProgressRequest;
import joel.thierry.bookshelf.model.BookProgress;
import joel.thierry.bookshelf.repository.BookProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookProgressService {
    private final BookProgressRepository bookProgressRepository;

    public BookProgressService(BookProgressRepository bookProgressRepository) {
        this.bookProgressRepository = bookProgressRepository;
    }

    public BookProgress saveProgress(BookProgressRequest request) {
        BookProgress progress = new BookProgress(
                null, // id will be generated
                request.getUserId(),
                request.getBookId(),
                request.getStatus()
        );
        return bookProgressRepository.save(progress);
    }

    public List<BookProgress> getProgressByUser(String userId) {
        return bookProgressRepository.findAll()
                .stream()
                .filter(bp -> bp.getUserId().equals(userId))
                .toList();
    }

    public Optional<BookProgress> getProgressById(String id) {
        return bookProgressRepository.findById(id);
    }
}