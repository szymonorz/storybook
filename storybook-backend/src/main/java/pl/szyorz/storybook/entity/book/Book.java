package pl.szyorz.storybook.entity.book;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.szyorz.storybook.entity.chapter.Chapter;
import pl.szyorz.storybook.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private String title;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book", fetch = FetchType.EAGER)
    @OrderBy("position ASC")
    @EqualsAndHashCode.Exclude
    private List<Chapter> chapters = new ArrayList<>();

    /* No collaboration is allowed, sorry */
    @ManyToOne
    private User author;
}
