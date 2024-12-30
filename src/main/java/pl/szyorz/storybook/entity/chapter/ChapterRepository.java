package pl.szyorz.storybook.entity.chapter;

import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChapterRepository extends Slice<Chapter> {

    List<Chapter> findAll();
    Optional<Chapter> findById(UUID id);
    Optional<Chapter> findByIdAndBookId(UUID id, UUID bookId);
    Optional<Chapter> findByBookIdAndPosition(UUID bookId, int position);
    Chapter save(Chapter chapter);
}
