package pl.szyorz.storybook.entity.book;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.szyorz.storybook.entity.book.data.CreateBookRequest;
import pl.szyorz.storybook.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

//    @GetMapping("/api/book/{bookId}")
//    @PreAuthorize("hasRole('ANONYMOUS')")
//    public ResponseEntity<Book> getBook(@PathVariable("bookId") UUID id) {
//
//    }

    /*
        Return logged in user's books
     */
    @GetMapping("/api/user/books")
    public ResponseEntity<List<Book>> currentUserBooks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication;

        return ResponseEntity.ok(bookService.getBooksByUserId(user.getId()));
    }

    @GetMapping("/api/user/{userId}/books")
    public ResponseEntity<List<Book>> userBooks(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(bookService.getBooksByUserId(userId));
    }

    @GetMapping("/api/book/{bookId}")
    public ResponseEntity<Book> bookById(@PathVariable("bookId") UUID bookId) {
        return bookService.getBookById(bookId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/api/book")
    public ResponseEntity<Book> createBook(@RequestBody CreateBookRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Optional<Book> bookOptional = bookService.createBook(request, user);

        return bookOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

}
