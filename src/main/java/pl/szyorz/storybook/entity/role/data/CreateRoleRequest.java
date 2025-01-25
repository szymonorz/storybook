package pl.szyorz.storybook.entity.role.data;

import pl.szyorz.storybook.entity.role.privilege.RolePrivilege;

import java.util.List;

public record CreateRoleRequest(String name, String description, List<RolePrivilege> privileges) {
}
