package pl.szyorz.storybook.entity.book.data;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record UpdateChaptersOrderRequest(
        @NotNull  List<UUID> chapterIdsInOrder) {
}
