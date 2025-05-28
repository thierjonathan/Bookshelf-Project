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

    public JsonNode getBooksByTitle(String title){
        return webClient.get()
                .uri("/volumes?q=intitle:" + title)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
    public JsonNode getBooksByAuthor(String author){
        return webClient.get()
                .uri("/volumes?q=inauthor:\"" + author + "\"")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

}
