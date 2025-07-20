package pl.szyorz.storybook.entity.book;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szyorz.storybook.entity.book.data.BookResponse;
import pl.szyorz.storybook.entity.book.data.CreateBookRequest;
import pl.szyorz.storybook.entity.book.data.NewBookChapterRequest;
import pl.szyorz.storybook.entity.chapter.data.ShortChapterResponse;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    /*
        Return logged in user's books
     */
    @GetMapping("/api/currentuser/books")
    public ResponseEntity<List<BookResponse>> currentUserBooks(Principal principal) {
        return ResponseEntity.ok(bookService.getBooksByUsername(principal.getName()));
    }

    @GetMapping("/api/user/{userId}/books")
    public ResponseEntity<List<BookResponse>> userBooks(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(bookService.getBooksByUserId(userId));
    }

    @PostMapping("/api/book/chapter")
    public ResponseEntity<ShortChapterResponse> createChapterInBook(@RequestBody NewBookChapterRequest request) {
        return bookService.addNewChapter(request)
                .map(c ->
                        ResponseEntity.ok(
                                new ShortChapterResponse(
                                        c.getId(),
                                        c.getTitle(),
                                        c.getDescription(),
                                        c.getPosition()))
                )
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/api/book/{bookId}")
    public ResponseEntity<BookResponse> bookById(@PathVariable("bookId") UUID bookId) {
        return bookService.getBookById(bookId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/api/book")
    public ResponseEntity<BookResponse> createBook(Principal principal, @RequestBody CreateBookRequest request) {
        Optional<BookResponse> bookOptional = bookService.createBook(request, principal.getName());

        return bookOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/api/book/latest")
    public ResponseEntity<List<BookResponse>> latest(Principal principal, @RequestParam(value = "n", defaultValue = "10", required = false) int n) {
        return ResponseEntity.ok(bookService.latest(n));
    }

    @GetMapping("/api/book/search")
    public ResponseEntity<List<BookResponse>> search(Principal principal,
                                                     @RequestParam(value = "q") String lookup,
                                                     @RequestParam(value = "n", defaultValue = "10", required = false) int n) {
        return ResponseEntity.ok(bookService.search(lookup, n));
    }
}
