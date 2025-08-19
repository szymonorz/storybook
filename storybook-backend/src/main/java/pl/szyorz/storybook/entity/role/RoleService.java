package pl.szyorz.storybook.entity.role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.role.data.CreateRoleRequest;
import pl.szyorz.storybook.entity.role.data.RoleResponse;

import java.util.*;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    public Optional<Role> createRole(CreateRoleRequest req) {
        Role reqRole = convertAPIRoleToDBRole(req);

        Role role = repository.save(reqRole);
        return Optional.of(role);
    }

    public List<Role> getRolesByNames(List<String> roleNames) {
        return repository.findByNameIn(roleNames);
    }

    public List<Role> getUserRoles(UUID userId) {
        return repository.findAllByUsersId(userId);
    }

    public List<RoleResponse> getAllRoles() {
        return repository.findAll()
                .stream()
                .map(this::convertDBtoAPIRole)
                .toList();
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
