package pl.szyorz.storybook.entity.book;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.szyorz.storybook.entity.chapter.Chapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    @OrderBy("position ASC")
    @EqualsAndHashCode.Exclude
    private List<Chapter> chapters = new ArrayList<>();
}