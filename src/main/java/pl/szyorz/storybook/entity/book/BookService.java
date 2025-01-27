package pl.szyorz.storybook.entity.book;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.book.data.BookResponse;
import pl.szyorz.storybook.entity.book.data.ChapterOrderUpdateRequest;
import pl.szyorz.storybook.entity.book.data.CreateBookRequest;
import pl.szyorz.storybook.entity.book.data.NewBookChapterRequest;
import pl.szyorz.storybook.entity.chapter.Chapter;
import pl.szyorz.storybook.entity.chapter.ChapterRepository;
import pl.szyorz.storybook.entity.chapter.data.ShortChapterResponse;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserRepository;
import pl.szyorz.storybook.entity.user.data.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookService {
    private BookRepository bookRepository;
    private ChapterRepository chapterRepository;
    private UserRepository userRepository;

    public Optional<BookResponse> createBook(CreateBookRequest req, String author) {
        User user = userRepository.findByUsername(author)
                .orElseThrow();
        Book book = mapToBookEntity(req, user);
        Book saved = bookRepository.save(book);
        return Optional.of(mapToBookResponse(saved, user));
    }

    public Optional<BookResponse> getBookById(UUID bookId) {
        return bookRepository.findById(bookId)
                .map(book -> mapToBookResponse(book, book.getAuthor()));
    }

    public List<Book> getBooksByUserId(UUID userId) {
        return bookRepository.findAllByAuthorId(userId);
    }

    public List<Book> getBooksByUsername(String username) {
        return bookRepository.findAllByAuthorUsername(username);
    }

    @Transactional
    public Optional<Chapter> addNewChapter(NewBookChapterRequest bookChapterRequest) {
        Optional<Book> bookOptional = bookRepository.findById(bookChapterRequest.bookId());
        if (bookOptional.isEmpty()) {
            throw new IllegalStateException("No book of id: " + bookChapterRequest.bookId() +" was found!");
        }
        Book book = bookOptional.get();
        Chapter chapter = mapToChapterEntity(bookChapterRequest);
        chapter.setBook(book);
        chapter.setPosition(book.getChapters().size() + 1);
        book.getChapters().add(chapter);

        Book updatedBook = bookRepository.save(book);

        return updatedBook
                .getChapters()
                .stream()
                .filter(c -> c.getPosition() == chapter.getPosition())
                .findFirst();
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
                dto.authorNote(),
                dto.chapterContent()
        );
    }

    private Book mapToBookEntity(CreateBookRequest req, User author) {
        Book book = new Book();
        book.setTitle(req.title());
        book.setDescription(req.description());
        book.setAuthor(author);

        return book;
    }

    private BookResponse mapToBookResponse(Book book, User author) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                new UserResponse(
                        author.getId(),
                        author.getUsername()
                ),
                book.getChapters().stream().map(
                        chapter -> new ShortChapterResponse(
                                chapter.getId(),
                                chapter.getTitle(),
                                chapter.getDescription(),
                                chapter.getPosition()
                        )
                ).toList()
        );
    }
}
