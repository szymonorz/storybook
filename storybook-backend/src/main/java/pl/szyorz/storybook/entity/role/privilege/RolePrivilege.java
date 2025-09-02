package pl.szyorz.storybook.entity.role.privilege;

public enum RolePrivilege {
    SUPERUSER, // has all privileges, designed only for testing
    CREATE_ROLE,
    MODIFY_ROLE,
    DELETE_ROLE,
    VIEW_ROLE,
    MODERATE_CONTENT,
    MODERATE_USERS,
    VIEW_REPORTS
}
