package pl.szyorz.storybook.entity.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import pl.szyorz.storybook.entity.book.Book;

import java.sql.Types;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(unique = true)
    private String username;
    private String password;
    private String email;

    @OneToMany
    private Set<Book> books;
}
