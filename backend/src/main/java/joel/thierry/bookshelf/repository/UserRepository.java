package joel.thierry.bookshelf.repository;

import joel.thierry.bookshelf.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    // Below: Return _id directly as a String — Avoid nested object issues
    @Query(value = "{ 'username': ?0 }", fields = "{ '_id': 1 }")
    Optional<UserIdProjection> findIdByUsername(String username);

    interface UserIdProjection {
        String getId(); // This will map the "_id" field correctly
    }
}
