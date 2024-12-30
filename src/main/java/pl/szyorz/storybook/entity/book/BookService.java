package pl.szyorz.storybook.entity.book;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.book.data.ChapterOrderUpdateRequest;
import pl.szyorz.storybook.entity.book.data.NewBookChapterRequest;
import pl.szyorz.storybook.entity.chapter.Chapter;
import pl.szyorz.storybook.entity.chapter.ChapterRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {
    private BookRepository bookRepository;
    private ChapterRepository chapterRepository;

    @Transactional
    public void addNewChapter(NewBookChapterRequest bookChapterRequest) {
        Optional<Book> bookOptional = bookRepository.findById(bookChapterRequest.bookId());
        if (bookOptional.isEmpty()) {
            throw new IllegalStateException("No book of id: " + bookChapterRequest.bookId() +" was found!");
        }
        Book book = bookOptional.get();
        Chapter chapter = mapToChapterEntity(bookChapterRequest);
        chapter.setBook(book);
        chapter.setPosition(book.getChapters().size() + 1);
        book.getChapters().add(chapter);

        bookRepository.save(book);
    }

    @Transactional
    public void updateChapterPosition(ChapterOrderUpdateRequest req) {
        Chapter chapter = chapterRepository.findByIdAndBookId(req.chapterId(), req.bookId())
                .orElseThrow(() -> new IllegalArgumentException("No chapter of id: " + req.chapterId() + " and book_id: " + req.bookId() +" found"));
        Book book = bookRepository.findById(req.bookId())
                .orElseThrow(() -> new IllegalArgumentException("No book of id: " + req.bookId() + " found"));

        int desiredPosition = req.desiredPosition();
        book.getChapters().remove(chapter);
        book.getChapters().forEach(c -> {
            if (c.getPosition() >= desiredPosition)
                c.setPosition(c.getPosition() + 1);
        });
        chapter.setPosition(desiredPosition);
        book.getChapters().add(desiredPosition - 1, chapter);
        for(int i=0; i<book.getChapters().size(); i++)
            book.getChapters().get(i).setPosition(i + 1);

        bookRepository.save(book);
    }

    /* Map the record class to entity class */
    private Chapter mapToChapterEntity(NewBookChapterRequest dto) {
        return new Chapter(
                dto.chapterTitle(),
                dto.chapterDescription(),
                dto.chapterContent()
        );
    }
}
