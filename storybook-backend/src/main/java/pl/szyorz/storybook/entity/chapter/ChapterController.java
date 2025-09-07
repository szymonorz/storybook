package pl.szyorz.storybook.entity.chapter;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szyorz.storybook.entity.chapter.data.ChapterContentResponse;
import pl.szyorz.storybook.entity.chapter.data.UpdateChapterRequest;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;


    @GetMapping("/api/book/{bookId}/chapter/{chapterNumber}")
    public ResponseEntity<ChapterContentResponse> getChapterContent(
            @PathVariable("bookId") UUID bookId,
            @PathVariable("chapterNumber") int chapterNumber
    )
    {
        return chapterService.getChapterContent(bookId, chapterNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PatchMapping(value = "/api/chapter/{chapterId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChapterContentResponse> updateChapter(
            @PathVariable("chapterId") UUID chapterId,
            @Valid @RequestBody UpdateChapterRequest req
    ) {
        return chapterService.updateChapter(chapterId, req)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
