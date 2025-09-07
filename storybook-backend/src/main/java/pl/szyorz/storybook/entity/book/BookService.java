package pl.szyorz.storybook.entity.book;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.book.data.*;
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
        book.setUpdatedAt(LocalDateTime.now());

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

    @Transactional
    public Optional<List<ShortChapterResponse>> reorderChapters(UUID bookId, List<UUID> orderedIds) {
        if (orderedIds == null) return Optional.empty();

        var bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) return Optional.empty();

        List<Chapter> all = chapterRepository.findAllByBookIdOrderByPositionAsc(bookId);

        Map<UUID, Chapter> byId = all.stream().collect(Collectors.toMap(Chapter::getId, c -> c));

        for (UUID id : orderedIds) {
            if (!byId.containsKey(id)) {
                return Optional.empty();
            }
        }

        List<Chapter> newOrder = new ArrayList<>(orderedIds.size() + Math.max(0, all.size() - orderedIds.size()));
        for (UUID id : orderedIds) newOrder.add(byId.get(id));

        Set<UUID> provided = new HashSet<>(orderedIds);
        for (Chapter ch : all) {
            if (!provided.contains(ch.getId())) newOrder.add(ch);
        }

        int pos = 1;
        for (Chapter ch : newOrder) {
            ch.setPosition(pos++);
        }

        chapterRepository.saveAll(newOrder);

        List<ShortChapterResponse> response = newOrder.stream()
                .map(ch -> new ShortChapterResponse(
                        ch.getId(),
                        ch.getTitle(),
                        ch.getDescription(),
                        ch.getPosition()
                ))
                .toList();

        return Optional.of(response);
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
            "hasAuthority('SUPERUSER') or @userSecurity.isBookAuthor(#bookId, authentication)"
    )
    public Optional<BookResponse> updateBook(
            UUID bookId,
            UpdateBookRequest req
    ) {
        Optional<Book> ob = bookRepository.findById(bookId);
        if (ob.isEmpty()) return Optional.empty();

        Book b = ob.get();
        if (req.title() != null) b.setTitle(req.title());
        if (req.description() != null) b.setDescription(req.description());

        Book saved = bookRepository.save(b);

        return Optional.of(new BookResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                new UserResponse(
                        saved.getAuthor().getId(), saved.getAuthor().getUsername()
                ),
                saved.getChapters().stream().map(c ->
                        new ShortChapterResponse(
                                c.getId(), c.getTitle(), c.getDescription(), c.getPosition()
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
