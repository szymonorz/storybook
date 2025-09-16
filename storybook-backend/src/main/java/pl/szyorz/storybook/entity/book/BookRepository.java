package pl.szyorz.storybook.entity.book;

import org.springframework.data.domain.Pageable;
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
                SELECT b FROM Book b
                JOIN b.chapters c
                GROUP BY b
                ORDER BY MAX(c.updatedAt), MAX(c.createdAt)
            """
    )
    List<Book> latest(Pageable pageable);

    @Query("""
        SELECT b
        FROM Book b
        LEFT JOIN b.chapters c
        LEFT JOIN b.author a
        WHERE
             LOWER(a.username)   LIKE LOWER(:lookup)
          OR LOWER(b.title)       LIKE LOWER(:lookup)
          OR LOWER(b.description) LIKE LOWER(:lookup)
          OR LOWER(c.title)       LIKE LOWER(:lookup)
          OR LOWER(c.description) LIKE LOWER(:lookup)
          OR LOWER(c.content)     LIKE LOWER(:lookup)
        GROUP BY b
        ORDER BY COALESCE(MAX(c.updatedAt), b.updatedAt) DESC, b.createdAt DESC
            """)
    List<Book> search(@Param("lookup") String lookup, Pageable pageable);
}
