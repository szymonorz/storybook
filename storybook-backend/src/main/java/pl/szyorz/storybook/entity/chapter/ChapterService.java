package pl.szyorz.storybook.entity.chapter;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.chapter.data.ChapterContentResponse;
import pl.szyorz.storybook.entity.chapter.data.UpdateChapterRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChapterService {
    private ChapterRepository chapterRepository;

    public List<Chapter> getAll() {
        return chapterRepository.findAll();
    }

    public Optional<ChapterContentResponse> getChapterContent(UUID bookId, int position) {
        return chapterRepository.findByBookIdAndPosition(bookId, position)
                .map(this::mapToContentResponse);
    }

    public Optional<Chapter> getNextChapter(UUID bookId, int position) {
        return chapterRepository.findByBookIdAndPosition(bookId, position +1);
    }

    public Optional<Chapter> getPreviousChapter(UUID bookId, int position) {
        return chapterRepository.findByBookIdAndPosition(bookId, position - 1);
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

    public Optional<ChapterContentResponse> updateChapter(UUID chapterId, UpdateChapterRequest req) {
        return chapterRepository.findById(chapterId).map(ch -> {
            if (req.title() != null) ch.setTitle(req.title());
            if (req.description() != null) ch.setDescription(req.description());
            if (req.authorsNote() != null) ch.setAuthorNote(req.authorsNote());
            if (req.content() != null) ch.setContent(req.content());
            Chapter saved = chapterRepository.save(ch);
            return new ChapterContentResponse(
                    saved.getId(),
                    saved.getTitle(),
                    saved.getDescription(),
                    saved.getAuthorNote(),
                    saved.getContent(),
                    saved.getPosition()
            );
        });
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
