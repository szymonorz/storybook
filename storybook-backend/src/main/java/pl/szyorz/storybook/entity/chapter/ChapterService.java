package pl.szyorz.storybook.entity.chapter;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.chapter.data.ChapterContentResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChapterService {
    private ChapterRepository repository;

    public List<Chapter> getAll() {
        return repository.findAll();
    }

    public Optional<ChapterContentResponse> getChapterContent(UUID bookId, int position) {
        return repository.findByBookIdAndPosition(bookId, position)
                .map(this::mapToContentResponse);
    }

    public Optional<Chapter> getNextChapter(UUID bookId, int position) {
        return repository.findByBookIdAndPosition(bookId, position +1);
    }

    public Optional<Chapter> getPreviousChapter(UUID bookId, int position) {
        return repository.findByBookIdAndPosition(bookId, position - 1);
    }

    private ChapterContentResponse mapToContentResponse(Chapter chapter) {
        return new ChapterContentResponse(
                chapter.getId(),
                chapter.getTitle(),
                chapter.getDescription(),
                chapter.getAuthorNote(),
                chapter.getContent(),
                chapter.getPosition()
        );
    }

    /* Map the record class to entity class */
    private Chapter mapToChapterEntity(ChapterRequest dto) {
        return new Chapter(
                dto.name(),
                dto.description(),
                dto.authorNote(),
                dto.content()
        );
    }
}
