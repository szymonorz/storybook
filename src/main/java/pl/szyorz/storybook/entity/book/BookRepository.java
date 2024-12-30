package pl.szyorz.storybook.entity.book;

import org.springframework.data.domain.Slice;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends Slice<Book> {
    Optional<Book> findById(UUID id);
    Book save(Book book);
}
