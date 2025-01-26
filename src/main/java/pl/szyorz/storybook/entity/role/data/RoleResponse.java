package pl.szyorz.storybook.entity.role.data;

import pl.szyorz.storybook.entity.role.privilege.RolePrivilege;

import java.util.List;
import java.util.UUID;

public record RoleResponse(UUID id, String name, String description, List<RolePrivilege> privileges) {
}
