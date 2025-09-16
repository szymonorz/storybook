package pl.szyorz.storybook.entity.chapter;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szyorz.storybook.entity.book.Book;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Chapter {
    public Chapter(String title, String description, String authorNote, String content) {
        this.title = title;
        this.description = description;
        this.authorNote = authorNote;
        this.content = content;
    }

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Book book;

    private String title;
    private String description;
    private String authorNote;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int position;


    // Only UUID matters
    // Plagiarism shouldn't cause server-side errors
    // TODO: test this
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chapter)) return false;
        Chapter that = (Chapter) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
