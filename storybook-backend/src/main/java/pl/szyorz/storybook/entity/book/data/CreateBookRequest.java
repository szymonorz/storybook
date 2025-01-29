package pl.szyorz.storybook.entity.book.data;

import java.util.List;

public record CreateBookRequest(String title, String description, List<String> tags, List<String> keywords) {
}
