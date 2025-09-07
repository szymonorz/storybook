package pl.szyorz.storybook.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.szyorz.storybook.entity.book.Book;
import pl.szyorz.storybook.entity.book.BookRepository;
import pl.szyorz.storybook.entity.chapter.ChapterRepository;
import pl.szyorz.storybook.entity.user.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Component("userSecurity")
@AllArgsConstructor
public class UserSecurity {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;

    public boolean isSelf(UUID userId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) return false;
        return userRepository.findById(userId)
                .map(u -> u.getUsername().equals(authentication.getName()))
                .orElse(false);
    }

    public boolean isBookAuthor(UUID bookId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) return false;
        return bookRepository.findById(bookId)
                .map(b -> b.getAuthor().getUsername().equals(authentication.getName()))
                .orElse(false);
    }

    public boolean isChapterAuthor(UUID chapterId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) return false;
        return chapterRepository.findById(chapterId)
                .map(c -> c.getBook().getAuthor().getUsername().equals(authentication.getName()))
                .orElse(false);
    }
}
