package pl.szyorz.storybook.entity.book;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.book.data.*;
import pl.szyorz.storybook.entity.book.exception.BookNotFoundException;
import pl.szyorz.storybook.entity.chapter.Chapter;
import pl.szyorz.storybook.entity.chapter.ChapterRepository;
import pl.szyorz.storybook.entity.chapter.data.ShortChapterResponse;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserRepository;
import pl.szyorz.storybook.entity.user.data.UserResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        Book saved = bookRepository.save(book);
        return Optional.of(mapToBookResponse(saved, user));
    }

    public Optional<BookResponse> findBookById(UUID bookId) {
        return bookRepository.findById(bookId)
                .map(book -> mapToBookResponse(book, book.getAuthor()));
    }

    public List<BookResponse> findBooksByUserId(UUID userId) {
        return bookRepository.findAllByAuthorId(userId)
                .stream().map(book -> mapToBookResponse(book, book.getAuthor())).toList();
    }

    public List<BookResponse> findBooksByUsername(String username) {
        return bookRepository.findAllByAuthorUsername(username)
                .stream().map(book -> mapToBookResponse(book, book.getAuthor())).toList();
    }

    @Transactional
    @PreAuthorize("@userSecurity.isBookAuthor(#bookId, authentication)")
    public void deleteBook(UUID bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        bookOptional.ifPresentOrElse( book -> bookRepository.delete(book), () -> {
            throw new BookNotFoundException("Book not found");
        });
        bookRepository.deleteById(bookId);
    }

    @Transactional
    public Optional<Chapter> addNewChapter(NewBookChapterRequest bookChapterRequest) {
        Optional<Book> bookOptional = bookRepository.findById(bookChapterRequest.bookId());
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("No book of id: " + bookChapterRequest.bookId() +" was found!");
        }
        Book book = bookOptional.get();
        Chapter chapter = mapToChapterEntity(bookChapterRequest);
        chapter.setBook(book);
        chapter.setPosition(book.getChapters().size() + 1);
        LocalDateTime now = LocalDateTime.now();
        chapter.setCreatedAt(now);
        chapter.setUpdatedAt(now);
        book.getChapters().add(chapter);
        book.setUpdatedAt(now);

        Book updatedBook = bookRepository.save(book);

        return updatedBook
                .getChapters()
                .stream()
                .filter(c -> c.getPosition() == chapter.getPosition())
                .findFirst();
    }

    public List<BookResponse> latest(int n) {
        return bookRepository.latest(n)
                .stream().map(book -> mapToBookResponse(book, book.getAuthor())).toList();
    }

    public List<BookResponse> search(String lookup, int n) {
        String wildcardLookup = '%' + lookup + '%';
        return bookRepository.search(wildcardLookup, n)
                .stream().map(book -> mapToBookResponse(book, book.getAuthor())).toList();
    }

    @PreAuthorize(
            "@userSecurity.isBookAuthor(#bookId, authentication)"
    )
    public Optional<BookResponse> updateBook(
            UUID bookId,
            UpdateBookRequest req
    ) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) throw new BookNotFoundException("No book of id: " + bookId + " was found.");

        Book book = bookOptional.get();
        if (req.title() != null) book.setTitle(req.title());
        if (req.description() != null) book.setDescription(req.description());

        Book saved = bookRepository.save(book);

        return Optional.of(new BookResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                new UserResponse(
                        saved.getAuthor().getId(), saved.getAuthor().getUsername()
                ),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getChapters().stream().map(c ->
                        new ShortChapterResponse(
                                c.getId(), c.getTitle(), c.getDescription(), c.getAuthorNote(), c.getPosition()
                        )
                ).toList()
        ));
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
                book.getCreatedAt(),
                book.getUpdatedAt(),
                book.getChapters().stream().map(
                        chapter -> new ShortChapterResponse(
                                chapter.getId(),
                                chapter.getTitle(),
                                chapter.getDescription(),
                                chapter.getAuthorNote(),
                                chapter.getPosition()
                        )
                ).toList()
        );
    }
}
