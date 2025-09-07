package pl.szyorz.storybook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szyorz.storybook.entity.book.Book;
import pl.szyorz.storybook.entity.book.BookRepository;
import pl.szyorz.storybook.entity.book.BookService;
import pl.szyorz.storybook.entity.book.data.BookResponse;
import pl.szyorz.storybook.entity.book.data.CreateBookRequest;
import pl.szyorz.storybook.entity.chapter.Chapter;
import pl.szyorz.storybook.entity.chapter.ChapterRepository;
import pl.szyorz.storybook.entity.chapter.data.ShortChapterResponse;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTests {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private ChapterRepository chapterRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookService bookService;

    private UUID bookId;
    private Chapter ch1, ch2, ch3;
    private User author;
    private UUID authorId;

    @Captor
    private ArgumentCaptor<Iterable<Chapter>> saveAllCaptor;
    @Captor
    private ArgumentCaptor<String> searchLookupCaptor;

    @BeforeEach
    void setUp() {
        authorId = UUID.randomUUID();
        author = new User();
        author.setId(authorId);
        author.setUsername("adam");

        bookId = UUID.randomUUID();

        ch1 = new Chapter(); ch1.setId(UUID.randomUUID()); ch1.setTitle("One"); ch1.setDescription("D1"); ch1.setPosition(1);
        ch2 = new Chapter(); ch2.setId(UUID.randomUUID()); ch2.setTitle("Two"); ch2.setDescription("D2"); ch2.setPosition(2);
        ch3 = new Chapter(); ch3.setId(UUID.randomUUID()); ch3.setTitle("Three"); ch3.setDescription("D3"); ch3.setPosition(3);

        lenient().when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        lenient().when(chapterRepository.findAllByBookIdOrderByPositionAsc(bookId)).thenReturn(List.of(ch1, ch2, ch3));
        lenient().when(chapterRepository.saveAll(any(Iterable.class))).thenAnswer(inv -> {
            Iterable<Chapter> it = inv.getArgument(0);
            List<Chapter> out = new ArrayList<>();
            for (Chapter c : it) out.add(c);
            return out;
        });
    }

    @Test
    void createBook_success_mapsAndSaves_andReturnsResponse() {
        // given
        CreateBookRequest req = new CreateBookRequest("Title", "Desc");
        when(userRepository.findByUsername("adam")).thenReturn(Optional.of(author));

        // bookRepository.save should return a persisted book (id + timestamps etc.)
        AtomicReference<Book> savedRef = new AtomicReference<>();
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> {
            Book b = inv.getArgument(0);
            b.setId(UUID.randomUUID());
            // Service sets timestamps; simulate persistence echo
            if (b.getCreatedAt() == null) b.setCreatedAt(LocalDateTime.now());
            savedRef.set(b);
            return b;
        });

        // when
        var out = bookService.createBook(req, "adam");

        // then
        assertTrue(out.isPresent());
        BookResponse br = out.get();
        assertNotNull(br.id());
        assertEquals("Title", br.title());
        assertEquals("Desc", br.description());
        assertNotNull(savedRef.get().getCreatedAt(), "createdAt should be set");
        assertEquals(authorId, br.author().id());
        assertEquals("adam", br.author().username());

        verify(userRepository).findByUsername("adam");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_returnsEmpty_whenAuthorNotFound() {
        CreateBookRequest req = new CreateBookRequest("T", "D");
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookService.createBook(req, "ghost"));

        verify(bookRepository, never()).save(any());
    }

    @Test
    void latest_mapsRepositoryResultsToResponses() {
        // given
        Book b1 = new Book(); b1.setId(UUID.randomUUID()); b1.setTitle("A"); b1.setDescription("DA"); b1.setAuthor(author);
        Book b2 = new Book(); b2.setId(UUID.randomUUID()); b2.setTitle("B"); b2.setDescription("DB"); b2.setAuthor(author);

        when(bookRepository.latest(2)).thenReturn(List.of(b1, b2));

        // when
        var list = bookService.latest(2);

        // then
        assertEquals(2, list.size());
        assertEquals(b1.getId(), list.get(0).id());
        assertEquals("A", list.get(0).title());
        assertEquals("DA", list.get(0).description());
        assertEquals(authorId, list.get(0).author().id());
        assertEquals("B", list.get(1).title());
    }

    @Test
    void search_wrapsLookupWithWildcards_andMapsResults() {
        Book b = new Book(); b.setId(UUID.randomUUID()); b.setTitle("Title match"); b.setDescription("desc"); b.setAuthor(author);

        when(bookRepository.search(anyString(), eq(5))).thenReturn(List.of(b));

        // when
        var results = bookService.search("match", 5);

        // then
        assertEquals(1, results.size());
        assertEquals(b.getId(), results.get(0).id());

        verify(bookRepository).search(searchLookupCaptor.capture(), eq(5));
        String used = searchLookupCaptor.getValue();
        assertTrue(used.startsWith("%") && used.endsWith("%"), "lookup should be wrapped with %");
        assertTrue(used.contains("match"));
    }

    @Test
    void reorderChapters_movesSpecifiedThenAppendsOthers() {
        List<UUID> order = List.of(ch3.getId(), ch1.getId());

        var resultOpt = bookService.reorderChapters(bookId, order);
        assertTrue(resultOpt.isPresent());

        List<ShortChapterResponse> res = resultOpt.get();
        assertEquals(3, res.size());
        assertEquals(ch3.getId(), res.get(0).id()); assertEquals(1, res.get(0).position());
        assertEquals(ch1.getId(), res.get(1).id()); assertEquals(2, res.get(1).position());
        assertEquals(ch2.getId(), res.get(2).id()); assertEquals(3, res.get(2).position());

        verify(chapterRepository).saveAll(saveAllCaptor.capture());
        List<Chapter> saved = new ArrayList<>();
        saveAllCaptor.getValue().forEach(saved::add);

        assertEquals(ch3.getId(), saved.get(0).getId()); assertEquals(1, saved.get(0).getPosition());
        assertEquals(ch1.getId(), saved.get(1).getId()); assertEquals(2, saved.get(1).getPosition());
        assertEquals(ch2.getId(), saved.get(2).getId()); assertEquals(3, saved.get(2).getPosition());
    }

    @Test
    void reorderChapters_returnsEmptyWhenUnknownChapterIdProvided() {
        var result = bookService.reorderChapters(bookId, List.of(UUID.randomUUID()));
        assertTrue(result.isEmpty());
        verify(chapterRepository, never()).saveAll(any());
    }

    @Test
    void reorderChapters_returnsEmptyWhenNullList() {
        var result = bookService.reorderChapters(bookId, null);
        assertTrue(result.isEmpty());
        verify(chapterRepository, never()).saveAll(any());
    }

    @Test
    void reorderChapters_returnsEmptyWhenBookMissing() {
        UUID other = UUID.randomUUID();
        when(bookRepository.findById(other)).thenReturn(Optional.empty());

        var result = bookService.reorderChapters(other, List.of(ch1.getId()));
        assertTrue(result.isEmpty());
        verify(chapterRepository, never()).saveAll(any());
    }
}

