package pl.szyorz.storybook.entity.chapter;

import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.szyorz.storybook.entity.book.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChapterRepository extends CrudRepository<Chapter, UUID> {
    List<Chapter> findAll();
    Optional<Chapter> findById(UUID id);
    Optional<Chapter> findByIdAndBookId(UUID id, UUID bookId);
    Optional<Chapter> findByBookIdAndPosition(UUID bookId, int position);
    List<Chapter> findAllByBookIdOrderByPositionAsc(UUID bookId);
    Chapter save(Chapter chapter);
}
