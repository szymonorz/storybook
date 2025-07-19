package pl.szyorz.storybook.entity.book;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    @Query(
            value = """
                SELECT DISTINCT b FROM Book b JOIN Chapter c ON b = c.book
                ORDER BY c.updatedAt, c.createdAt\s
                LIMIT :n\s        
            """
    )
    List<Book> latest(@Param("n") int n);
}
