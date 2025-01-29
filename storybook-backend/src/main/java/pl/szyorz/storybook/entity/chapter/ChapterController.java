package pl.szyorz.storybook.entity.chapter;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.szyorz.storybook.entity.chapter.data.ChapterContentResponse;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;


    @GetMapping("/api/book/{bookId}/chapter/{chapterNumber}")
    public ResponseEntity<ChapterContentResponse> getChapterContent(@PathVariable("bookId") UUID bookId,
                                                                    @PathVariable("chapterNumber") int chapterNumber)
    {
        return chapterService.getChapterContent(bookId, chapterNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
