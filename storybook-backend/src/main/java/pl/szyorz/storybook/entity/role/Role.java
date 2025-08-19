package pl.szyorz.storybook.entity.role;

import jakarta.persistence.*;
import lombok.Data;
import pl.szyorz.storybook.entity.role.privilege.RolePrivilege;
import pl.szyorz.storybook.entity.user.User;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String name;
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @ElementCollection(targetClass = RolePrivilege.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "role_privileges", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "privilege")
    @Enumerated(EnumType.STRING)
    private List<RolePrivilege> privileges;
}
