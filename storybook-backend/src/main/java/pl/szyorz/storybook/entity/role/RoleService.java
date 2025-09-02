package pl.szyorz.storybook.entity.role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.role.data.CreateRoleRequest;
import pl.szyorz.storybook.entity.role.data.RoleResponse;
import pl.szyorz.storybook.entity.role.privilege.RolePrivilege;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserRepository;
import pl.szyorz.storybook.entity.user.UserService;
import pl.szyorz.storybook.entity.user.data.DetailedUserResponse;
import pl.szyorz.storybook.entity.user.data.UserResponse;

import java.util.*;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Optional<Role> createRole(CreateRoleRequest req) {
        Role reqRole = convertAPIRoleToDBRole(req);

        Role role = roleRepository.save(reqRole);
        return Optional.of(role);
    }

    public List<String> listAllPrivileges() { return Arrays.stream(RolePrivilege.values()).map(Enum::name).toList(); }

    public List<Role> getRolesByNames(List<String> roleNames) {
        return roleRepository.findByNameIn(roleNames);
    }

    public List<Role> getUserRoles(UUID userId) {
        return roleRepository.findAllByUsersId(userId);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::convertDBtoAPIRole)
                .toList();
    }

    public List<DetailedUserResponse> findUsersByRole(UUID roleId) {
        return userRepository.findAllByRolesId(roleId).stream().map(UserService::convertDBUserToAPIUser).toList();
    }

    public void assignRoleToUser(UUID roleId, UUID userId) {
        Role role = roleRepository.findById(roleId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(UUID roleId, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.getRoles().removeIf(r -> r.getId().equals(roleId));
        userRepository.save(user);
    }

    public RoleResponse updatePrivileges(UUID roleId, List<String> privileges) {
        Role role = roleRepository.findById(roleId).orElseThrow();
        role.setPrivileges(
                privileges.stream()
                        .map(RolePrivilege::valueOf)
                        .toList()
        );
        Role saved = roleRepository.save(role);
        return convertDBtoAPIRole(saved);
    }

    private Role convertAPIRoleToDBRole(CreateRoleRequest req) {
        Role role = new Role();
        role.setName(req.name());
        role.setDescription(req.description());
        role.setPrivileges(req.privileges());
        return role;
    }

    private RoleResponse convertDBtoAPIRole(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getPrivileges()
        );
    }
}
