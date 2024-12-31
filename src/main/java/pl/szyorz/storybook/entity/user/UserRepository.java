package pl.szyorz.storybook.entity.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> save(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
