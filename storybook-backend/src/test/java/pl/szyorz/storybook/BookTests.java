package pl.szyorz.storybook;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.szyorz.storybook.entity.book.BookController;
import pl.szyorz.storybook.entity.book.BookService;
import pl.szyorz.storybook.entity.book.data.BookResponse;
import pl.szyorz.storybook.entity.book.data.CreateBookRequest;
import pl.szyorz.storybook.entity.book.data.NewBookChapterRequest;
import pl.szyorz.storybook.entity.chapter.Chapter;
import pl.szyorz.storybook.entity.chapter.data.ShortChapterResponse;
import pl.szyorz.storybook.entity.user.data.UserResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookResponse sampleBook(UUID id) {
        return new BookResponse(
                id,
                "Title " + id.toString().substring(0, 8),
                "Desc " + id.toString().substring(0, 8),
                new UserResponse(UUID.randomUUID(), "adam"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(new ShortChapterResponse(UUID.randomUUID(), "Ch 1", "Intro", null,1))
        );
    }

    @Test
    void getBookById_shouldReturn200WithBook() throws Exception {
        UUID id = UUID.randomUUID();
        BookResponse resp = sampleBook(id);
        given(bookService.findBookById(eq(id))).willReturn(Optional.of(resp));

        mockMvc.perform(get("/api/book/{bookId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value(resp.title()))
                .andExpect(jsonPath("$.author.username").value(resp.author().username()));
    }

    @Test
    void getBookById_shouldReturn400WhenMissing() throws Exception {
        UUID id = UUID.randomUUID();
        given(bookService.findBookById(eq(id))).willReturn(Optional.empty());

        mockMvc.perform(get("/api/book/{bookId}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_shouldReturn200OnSuccess() throws Exception {
        CreateBookRequest req = new CreateBookRequest("My Book", "About stuff");
        BookResponse created = sampleBook(UUID.randomUUID());
        given(bookService.createBook(any(CreateBookRequest.class), eq("adam"))).willReturn(Optional.of(created));

        mockMvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .principal(() -> "adam"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id().toString()))
                .andExpect(jsonPath("$.title").value(created.title()));
    }

    @Test
    void createBook_shouldReturn400OnFailure() throws Exception {
        CreateBookRequest req = new CreateBookRequest("My Book", "About stuff");
        given(bookService.createBook(any(CreateBookRequest.class), eq("adam"))).willReturn(Optional.empty());

        mockMvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .principal(() -> "adam"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addNewChapter_shouldReturn200WithShortChapter() throws Exception {
        NewBookChapterRequest req = new NewBookChapterRequest(UUID.randomUUID(), "Ch X", "Desc", "Note", "Content");
        Chapter chapter = new Chapter();
        chapter.setId(UUID.randomUUID());
        chapter.setPosition(1);
        chapter.setTitle(req.chapterTitle());
        chapter.setDescription(req.chapterDescription());
        chapter.setContent(req.chapterContent());
        given(bookService.addNewChapter(any(NewBookChapterRequest.class))).willAnswer(inv -> Optional.ofNullable(chapter));

        mockMvc.perform(post("/api/book/chapter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .principal(() -> "adam"))
                .andExpect(status().isOk());
    }

    @Test
    void latest_shouldReturnList() throws Exception {
        List<BookResponse> latest = List.of(sampleBook(UUID.randomUUID()), sampleBook(UUID.randomUUID()));
        given(bookService.latest(2)).willReturn(latest);

        mockMvc.perform(get("/api/book/latest").param("n", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value(latest.get(0).title()));
    }

    @Test
    void search_shouldReturnList() throws Exception {
        List<BookResponse> result = List.of(sampleBook(UUID.randomUUID()));
        given(bookService.search(eq("foo"), eq(5))).willReturn(result);

        mockMvc.perform(get("/api/book/search").param("q", "foo").param("n", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(result.get(0).title()));
    }
}

