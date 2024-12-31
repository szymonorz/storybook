package pl.szyorz.storybook.entity.user.data;

public record CreateUserRequest(String username, String email, String password) {
}
