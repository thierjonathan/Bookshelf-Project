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

    public BookProgress saveProgress(BookProgressRequest request, String authenticatedUserId) {
        // Ensure the progress being created is for the authenticated user
        if (!request.getUserId().equals(authenticatedUserId)) {
            throw new SecurityException("Cannot create progress for another user!");
        }

        Optional<BookProgress> existingProgress = bookProgressRepository.findByUserIdAndBookId(
                authenticatedUserId, request.getBookId()
        );

        if (existingProgress.isPresent()) {
            // Update existing progress
            BookProgress progress = existingProgress.get();
            progress.setStatus(request.getStatus()); // Update status
            return bookProgressRepository.save(progress);
        } else {
            // Create new progress for the authenticated user
            BookProgress progress = new BookProgress(
                    null,
                    authenticatedUserId,
                    request.getBookId(),
                    request.getStatus()
            );
            return bookProgressRepository.save(progress);
        }
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
    public void deleteProgress(String id){
        bookProgressRepository.deleteById(id);
    }
}