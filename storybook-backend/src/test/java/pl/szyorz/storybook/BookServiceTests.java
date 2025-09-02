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
import pl.szyorz.storybook.entity.chapter.Chapter;
import pl.szyorz.storybook.entity.chapter.ChapterRepository;
import pl.szyorz.storybook.entity.chapter.data.ShortChapterResponse;
import pl.szyorz.storybook.entity.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTests {

    @Mock private BookRepository bookRepository;
    @Mock
    private ChapterRepository chapterRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private BookService bookService;

    private UUID bookId;
    private Chapter ch1, ch2, ch3;

    @Captor
    private ArgumentCaptor<Iterable<Chapter>> saveAllCaptor;

    @BeforeEach
    void setUp() {
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
    void reorderChapters_happyPath_movesSpecifiedThenAppendsOthers() {
        List<UUID> order = List.of(ch3.getId(), ch1.getId());

        var resultOpt = bookService.reorderChapters(bookId, order);
        assertTrue(resultOpt.isPresent());

        List<ShortChapterResponse> res = resultOpt.get();
        assertEquals(3, res.size());
        assertEquals(ch3.getId(), res.get(0).id()); assertEquals(1, res.get(0).position());
        assertEquals(ch1.getId(), res.get(1).id()); assertEquals(2, res.get(1).position());
        assertEquals(ch2.getId(), res.get(2).id()); assertEquals(3, res.get(2).position());

        verify(chapterRepository).saveAll(saveAllCaptor.capture());
        List<Chapter> saved = new ArrayList<>(); saveAllCaptor.getValue().forEach(saved::add);

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

