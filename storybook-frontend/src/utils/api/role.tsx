
export interface UserRole {
    id: string,
    name: string,
    description: string,
    privileges: string[]
}

export enum RolePrivilege {
    SUPERUSER, // has all privileges, designed only for testing
    CREATE_ROLE,
    MODIFY_ROLE,
    DELETE_ROLE,
    VIEW_ROLE,
    MODERATE_CONTENT,
    MODERATE_USERS,
    VIEW_REPORTS
}