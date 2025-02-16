package pl.szyorz.storybook.entity.user;

import jakarta.persistence.*;
import lombok.Data;
import pl.szyorz.storybook.entity.book.Book;
import pl.szyorz.storybook.entity.role.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String username;
    private String password;
    private String email;

    @ManyToMany
    private List<Role> userRoles;

    @OneToMany
    private Set<Book> books;
}
