package pl.szyorz.storybook.entity.role.data;

import pl.szyorz.storybook.entity.role.privilege.RolePrivilege;

import java.util.Set;
import java.util.UUID;

public record UpdateRoleRequest(UUID roleId, String name, String description, Set<RolePrivilege> privileges) {
}
