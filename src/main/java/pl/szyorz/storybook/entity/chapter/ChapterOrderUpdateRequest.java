package pl.szyorz.storybook.entity.chapter;

import java.util.UUID;
/*
    Some assumptions:
        - if newPrevious isn't given -> move to the beginning
        - if newNext isn't given -> move to the end
 */
public record ChapterOrderUpdateRequest(UUID id,
                                        UUID newPrevious,
                                        UUID newNext) {
}
