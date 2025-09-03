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