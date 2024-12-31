package pl.szyorz.storybook.entity.role.data;

import pl.szyorz.storybook.entity.role.privilege.RolePrivilege;

import java.util.Set;

public record CreateRoleRequest(String name, String description, Set<RolePrivilege> privileges) {
}
