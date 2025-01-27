package pl.szyorz.storybook.entity.book;

import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<Book, UUID> {
    Optional<Book> findById(UUID id);
    Book save(Book book);
    List<Book> findAllByAuthorId(UUID authorId);
    List<Book> findAllByAuthorUsername(String username);
}
