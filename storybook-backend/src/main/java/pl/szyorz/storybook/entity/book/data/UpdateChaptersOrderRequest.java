package pl.szyorz.storybook.entity.book.data;

import java.util.List;
import java.util.UUID;

public record UpdateChaptersOrderRequest(List<UUID> chapterIdsInOrder) {
}
