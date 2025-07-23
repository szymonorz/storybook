package pl.szyorz.storybook.entity.chapter;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szyorz.storybook.entity.book.Book;

import java.time.LocalDateTime;
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
    /*
    TODO: Bind to user/author and also to book

    private Author author;
    private Book book; <--- dunno if this should be called a book
     */

    private String title;
    private String description;
    private String authorNote;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "previous_chapter")
//    private Chapter previousChapter;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "next_chapter")
//    private Chapter nextChapter;
    private int position;


    // Only UUID matters
    // Plagiarism shouldn't cause server-side errors
    // TODO: test this
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Chapter)) return false;
        return  this.id == ((Chapter)o).id;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result += this.id.hashCode();
        result += this.content.hashCode();
        result += this.description.hashCode();
        result += this.title.hashCode();

        return result;
    }

//    public boolean hasNextChapter() {
//        return this.nextChapter.getId() != null;
//    }
//
//    public boolean hasPreviousChapter() {
//        return previousChapter.getId() != null;
//    }
}
