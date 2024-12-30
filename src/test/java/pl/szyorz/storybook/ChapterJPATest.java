package pl.szyorz.storybook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.szyorz.storybook.entity.chapter.Chapter;
import pl.szyorz.storybook.entity.chapter.ChapterRepository;
import pl.szyorz.storybook.entity.chapter.ChapterRequest;
import pl.szyorz.storybook.entity.chapter.ChapterService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChapterJPATest {

    private ChapterRepository repository;

    private ChapterService service;

    private Chapter chapter1, chapter2;



    @BeforeEach
    void setUp() {

        // Mock Chapter entities
        chapter1 = mock(Chapter.class);
        chapter2 = mock(Chapter.class);
        repository = mock(ChapterRepository.class);

        service = new ChapterService(repository);


        // Mock the UUIDs
        UUID chapter1Id = UUID.randomUUID();
        UUID chapter2Id = UUID.randomUUID();

        // Set mocked UUIDs (assuming setter for ID exists in your entity)
        when(chapter1.getId()).thenReturn(chapter1Id);
        when(chapter2.getId()).thenReturn(chapter2Id);



    }

    /*
        Test that just verifies if the project compiles and starts.
        Accomplishes nothing beyond that
     */
    @Test
    void shouldReturnAllChapters() {
        //given
        Chapter chapter1 = new Chapter("First", "First chapter", "dasdasdasd");
        Chapter chapter2 = new Chapter("Second", "Second chapter", "dasdasdasd");
        chapter1.setPosition(1);
        chapter2.setPosition(2);
        when(repository.findAll()).thenReturn(List.of(chapter1, chapter2));

        assertEquals(List.of(chapter1, chapter2), service.getAll());
    }



    /*
        Given:
            A -> B -> C -> D
        Expected:
            A -> C -> B -> D
     */
    @Test
    void shouldUpdateBetween() {
        // Mockito isn't really for checking database persistence so.....
        // TODO: implement this, need in-memory database
    }

}
