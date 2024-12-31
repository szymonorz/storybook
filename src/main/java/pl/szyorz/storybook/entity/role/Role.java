package pl.szyorz.storybook.entity.role;

import jakarta.persistence.*;
import lombok.Data;
import pl.szyorz.storybook.entity.role.privilege.RolePrivilege;
import pl.szyorz.storybook.entity.user.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @ElementCollection(targetClass = RolePrivilege.class)
    @CollectionTable(name = "role_privileges", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "privilege")
    @Enumerated(EnumType.STRING)
    private Set<RolePrivilege> privileges;
}
