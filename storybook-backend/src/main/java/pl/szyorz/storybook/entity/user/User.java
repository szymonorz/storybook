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
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany
    private Set<Book> books;
}
