package pl.szyorz.storybook.entity.chapter;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChapterService {
    private ChapterRepository repository;


    public List<Chapter> getAll() {
        return repository.findAll();
    }

    /*
        General idea is that:

         If we are treating chapters as a linked list in a book then new chapter MUST always be appended to the end
         of the book. If the user wishes so then they can later update the order of chapters.
    */


    /*
        Add new chapter. New chapters are always appended at the end meaning they cannot have next_chapter.
        This is somewhat messy because it expects the UUID of the previous chapter to be provided within request.
        Instead, it should just check if book has previous chapter but im not sure if that isn't somewhat worse.
     */
    @Transactional
    public Optional<UUID> addNewChapter(ChapterRequest chapterRequest) {
        Chapter chapter = mapToChapterEntity(chapterRequest);

        Optional<Chapter> previousOptional = repository.findById(chapterRequest.previousChapter());
        if (previousOptional.isPresent()) {
            Chapter previous = previousOptional.get();
            chapter.setPreviousChapter(previous);
            previous.setNextChapter(chapter);

            Chapter next = repository.save(chapter);
            repository.save(previous);
            return Optional.of(next.getId());
        }

        Chapter saved = repository.save(chapter);
        return Optional.of(saved.getId());
    }

    /*
        Update order of the chapters
        NOTE: make sure to test it through.

        CASE: lest have chapters in order

        A -> B -> C -> D

        given request
        {
            id: C,
            new_previous: A,
            new_next: B
        }

        check:
        before update
            1. if C isn't already in desired place (no update required)
            2. if B is after A
        after update:
            1. if C is in desired place
            2. if B's next is D
            3. if D's previous is B

        so after the update it should look like
        A -> C -> B -> D

        edge cases (?)
            - moving the first chapter (A) further up the list
            - moving chapter to the beginning (in front of A) of the list


    TODO: better naming because on God these are horrible
     */
    @Transactional
    public Optional<UUID> updateOrder(ChapterOrderUpdateRequest request) {

        if (request.newPrevious() == null) return moveToTheBeginning(request);
        if (request.newNext() == null) return moveToTheEnd(request);
        return moveBetween(request);


    }


    private Optional<UUID> moveToTheBeginning(ChapterOrderUpdateRequest request) {
        return Optional.empty();
    }

    private Optional<UUID> moveToTheEnd(ChapterOrderUpdateRequest request) {
        return Optional.empty();
    }

    /*
        Hell on earth
     */
    private Optional<UUID> moveBetween(ChapterOrderUpdateRequest request) {
        Optional<Chapter> toUpdateOptional = repository.findById(request.id());
        Optional<Chapter> newPreviousOptional = repository.findById(request.newPrevious());
        Optional<Chapter> newNextOptional = repository.findById(request.newNext());
        /* These should throw individual exceptions. Too lazy to do so rn */
        if (toUpdateOptional.isEmpty()) {
            return Optional.empty();
        }
        if (newPreviousOptional.isEmpty()) {
            return Optional.empty();
        }
        if (newNextOptional.isEmpty()) {
            return Optional.empty();
        }

        Chapter toUpdate = toUpdateOptional.get();
        Chapter newPrevious = newPreviousOptional.get();
        Chapter newNext = newNextOptional.get();

        // Already in the desired place, no need to update
        // TODO: should it throw an exception?
        if (newPrevious.getNextChapter().equals(toUpdate) && newNext.getPreviousChapter().equals(toUpdate)) {
            return Optional.empty();
        }

        // can't insert between because they are not right after another
        // TODO: throw exception
        if (!newPrevious.getNextChapter().equals(newNext)) {
            return Optional.empty();
        }

        Chapter currentNext = toUpdate.getNextChapter().getId() == null ? toUpdate.getNextChapter() : null;
        Chapter currentPrevious = toUpdate.getPreviousChapter().getId() == null ? toUpdate.getPreviousChapter() : null;

        // toUpdate is between two chapters
        if (currentNext != null && currentPrevious != null) {
            currentPrevious.setNextChapter(currentNext);
            currentNext.setPreviousChapter(currentPrevious);

            repository.save(currentNext);
            repository.save(currentPrevious);
        } else if(currentNext != null) {
            // toUpdate is at the beginning (1st chapter)
            currentNext.setPreviousChapter(null);
            repository.save(currentNext);
        } else if (currentPrevious != null) {
            // toUpdate is at the end (latest chapter)
            currentPrevious.setNextChapter(null);
            repository.save(currentPrevious);
        }

        newNext.setPreviousChapter(toUpdate);
        newPrevious.setNextChapter(toUpdate);

        toUpdate.setNextChapter(newNext);
        toUpdate.setPreviousChapter(newPrevious);

        repository.save(toUpdate);
        repository.save(newNext);
        repository.save(newPrevious);

        return Optional.empty();
    }


    /* Map the record class to entity class */
    private Chapter mapToChapterEntity(ChapterRequest dto) {
        return new Chapter(
                dto.name(),
                dto.description(),
                dto.content()
        );
    }
}
