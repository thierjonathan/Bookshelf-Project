package joel.thierry.bookshelf.service;

import joel.thierry.bookshelf.mapper.Mapper;
import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.model.Book;
import joel.thierry.bookshelf.model.User;
import joel.thierry.bookshelf.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WebClient webClient;
    private final Mapper mapper;

    public UserService(UserRepository userRepository, WebClient.Builder webClientBuilder, Mapper mapper) {
        this.userRepository = userRepository;
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/books/v1").build();
        this.mapper = mapper;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User addFavoriteBook(String username, String BookId){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        //makes sure there is no duplicate
        if(!user.getFavoriteBook().contains(BookId)) {
            user.getFavoriteBook().add(BookId);
        }
        return userRepository.save(user);

    }

    public User addFavoriteAuthor(String username, String Authorname){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        if(!user.getFavoriteAuthor().contains(Authorname)) {
            user.getFavoriteAuthor().add(Authorname);
        }
        return userRepository.save(user);
    }
    public User addToWishlist(String username, String BookId){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        if (!user.getWishlist().contains(BookId)) {
            user.getCurrentlyReading().remove(BookId);
            user.getDoneReading().remove(BookId);
            user.getWishlist().add(BookId);
        }
        return userRepository.save(user);
    }
    public User addToCurrentlyReading(String username, String bookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getCurrentlyReading().contains(bookId)) {
            user.getWishlist().remove(bookId);
            user.getDoneReading().remove(bookId);

            user.getCurrentlyReading().add(bookId);
        }
        return userRepository.save(user);
    }

    public User addToDoneReading(String username, String bookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getDoneReading().contains(bookId)) {
            user.getWishlist().remove(bookId);
            user.getCurrentlyReading().remove(bookId);

            user.getDoneReading().add(bookId);
        }
        return userRepository.save(user);
    }

    public User removeFavoriteBook(String username, String BookId){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        user.getFavoriteBook().remove(BookId);
        return userRepository.save(user);
    }

    public User removeFavoriteAuthor(String username, String Authorname){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        user.getFavoriteAuthor().remove(Authorname);
        return userRepository.save(user);
    }

    public User removeWishlist(String username, String BookId){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        user.getWishlist().remove(BookId);
        return userRepository.save(user);
    }
    public User removeCurrentlyReading(String username, String BookId){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        user.getCurrentlyReading().remove(BookId);
        return userRepository.save(user);
    }
    public User removeDoneReading(String username, String BookId){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        user.getDoneReading().remove(BookId);
        return userRepository.save(user);
    }

    public List<BookDTO> getFavoriteBook(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        List<String> bookIds = user.getFavoriteBook();
        return this.extractBooksfromList(bookIds);
    }
    public List<BookDTO> getWishlist(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        List<String> bookIds = user.getWishlist();
        return this.extractBooksfromList(bookIds);
    }
    public List<BookDTO> getCurrentlyReading(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        List<String> bookIds = user.getCurrentlyReading();
        return this.extractBooksfromList(bookIds);
    }
    public List<BookDTO> getDoneReading(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        List<String> bookIds = user.getDoneReading();
        return this.extractBooksfromList(bookIds);
    }
    public List<BookDTO> extractBooksfromList(List<String> bookIds){
        List<BookDTO> books = new ArrayList<>();
        for(String bookId : bookIds) {
            String Url = "/volumes/" + bookId;
            Book book = webClient.get()
                    .uri(Url)
                    .retrieve()
                    .bodyToMono(Book.class)
                    .block();

            if(book != null) {
                books.add(mapper.convertToBookDTO(book));
            }

        }
        return books;
    }

}