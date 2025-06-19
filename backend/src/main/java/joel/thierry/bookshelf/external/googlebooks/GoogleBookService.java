package joel.thierry.bookshelf.external.googlebooks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GoogleBookService {
    private final WebClient webClient;

    public GoogleBookService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/books/v1").build();
    }

    public JsonNode searchBooks(String query) {
        return webClient.get()
                .uri("/volumes?q=" + query)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
    public String searchBooksRaw(String query) {
        return webClient.get()
                .uri("/volumes?q=" + query)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
