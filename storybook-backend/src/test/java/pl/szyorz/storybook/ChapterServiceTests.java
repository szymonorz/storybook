package pl.szyorz.storybook;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szyorz.storybook.entity.book.Book;
import pl.szyorz.storybook.entity.chapter.Chapter;
import pl.szyorz.storybook.entity.chapter.ChapterRepository;
import pl.szyorz.storybook.entity.chapter.ChapterService;
import pl.szyorz.storybook.entity.chapter.data.ChapterContentResponse;
import pl.szyorz.storybook.entity.chapter.data.UpdateChapterRequest;
import pl.szyorz.storybook.entity.chapter.exception.ChapterNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChapterServiceTests {

    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private ChapterService chapterService;

    @Test
    void getAll_returnsAllChaptersFromRepo() {
        Chapter c1 = new Chapter(); c1.setId(UUID.randomUUID()); c1.setTitle("A");
        Chapter c2 = new Chapter(); c2.setId(UUID.randomUUID()); c2.setTitle("B");
        when(chapterRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Chapter> out = chapterService.getAll();
        assertEquals(2, out.size());
        assertEquals("A", out.get(0).getTitle());
        verify(chapterRepository).findAll();
    }

    @Test
    void getChapterContent_mapsEntityToResponse() {
        UUID bookId = UUID.randomUUID();
        Chapter ch = new Chapter();
        ch.setId(UUID.randomUUID());
        ch.setTitle("T");
        ch.setDescription("D");
        ch.setAuthorNote("N");
        ch.setContent("C");
        ch.setPosition(5);

        when(chapterRepository.findByBookIdAndPosition(bookId, 3)).thenReturn(Optional.of(ch));

        Optional<ChapterContentResponse> out = chapterService.getChapterContent(bookId, 3);
        assertTrue(out.isPresent());
        ChapterContentResponse r = out.get();
        assertEquals(ch.getId(), r.id());
        assertEquals("T", r.title());
        assertEquals("D", r.description());
        assertEquals("N", r.authorsNote());
        assertEquals("C", r.content());
        assertEquals(5, r.position());
    }

    @Test
    void getNextChapter_delegatesWithPositionPlusOne() {
        UUID bookId = UUID.randomUUID();
        Chapter ch = new Chapter();
        when(chapterRepository.findByBookIdAndPosition(bookId, 11)).thenReturn(Optional.of(ch));

        Optional<Chapter> out = chapterService.getNextChapter(bookId, 10);
        assertTrue(out.isPresent());
        verify(chapterRepository).findByBookIdAndPosition(bookId, 11);
    }

    @Test
    void getPreviousChapter_delegatesWithPositionMinusOne() {
        UUID bookId = UUID.randomUUID();
        Chapter ch = new Chapter();
        when(chapterRepository.findByBookIdAndPosition(bookId, 9)).thenReturn(Optional.of(ch));

        Optional<Chapter> out = chapterService.getPreviousChapter(bookId, 10);
        assertTrue(out.isPresent());
        verify(chapterRepository).findByBookIdAndPosition(bookId, 9);
    }

    @Test
    void updateChapter_updatesOnlyProvidedFields_andSaves() {
        UUID chapterId = UUID.randomUUID();

        Book book = new Book();
        Chapter ch = new Chapter();
        ch.setId(chapterId);
        ch.setBook(book);
        ch.setTitle("Old");
        ch.setDescription("OldD");
        ch.setAuthorNote("OldN");
        ch.setContent("OldC");
        ch.setPosition(2);

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(ch));
        when(chapterRepository.save(any(Chapter.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateChapterRequest req = new UpdateChapterRequest("New", null, "NewNote", null);

        Optional<ChapterContentResponse> out = chapterService.updateChapter(chapterId, req);
        assertTrue(out.isPresent());

        ChapterContentResponse r = out.get();
        assertEquals("New", r.title());               // changed
        assertEquals("OldD", r.description());        // unchanged
        assertEquals("NewNote", r.authorsNote());      // changed
        assertEquals("OldC", r.content());            // unchanged
        assertEquals(2, r.position());

        verify(chapterRepository).save(any(Chapter.class));
    }

    @Test
    void updateChapter_returnsEmptyWhenChapterMissing() {
        when(chapterRepository.findById(any())).thenReturn(Optional.empty());
        UpdateChapterRequest req = new UpdateChapterRequest("X", null, null, null);

        Optional<ChapterContentResponse> out = chapterService.updateChapter(UUID.randomUUID(), req);
        assertTrue(out.isEmpty());
        verify(chapterRepository, never()).save(any());
    }

    @Test
    void shouldDelete() {
        UUID id = UUID.randomUUID();
        Chapter ch = new Chapter();
        ch.setId(id);

        when(chapterRepository.findById(id)).thenReturn(Optional.of(ch));

        chapterService.deleteChapter(id);

        verify(chapterRepository).findById(id);
        verify(chapterRepository).delete(ch);
        verify(chapterRepository).deleteById(id);
    }

    @Test
    void shouldThrowChapterNotFoundException() {
        UUID id = UUID.randomUUID();
        when(chapterRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ChapterNotFoundException.class, () -> chapterService.deleteChapter(id));

        verify(chapterRepository).findById(id);
        verify(chapterRepository, never()).delete(any());
        verify(chapterRepository, never()).deleteById(any());
    }
}
