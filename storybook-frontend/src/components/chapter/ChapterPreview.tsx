import { useTranslation } from "react-i18next";
import ShortChapterResponse, { deleteChapter } from "../../utils/api/chapter";
import { useNavigate, useParams } from "react-router";

interface ChapterPreviewProps {
    chapter: ShortChapterResponse,
    deleteCallback: () => void,
    isOwner: boolean
}

export default function ChapterPreview({chapter, isOwner, deleteCallback}: ChapterPreviewProps) {
    const {t} = useTranslation()
    const {bookId} = useParams()
    const navigate = useNavigate()

    function handleOnClick() {
        navigate(`/book/${bookId}/chapter/${chapter.position}`)
    }

    function handleDeleteChapter(chapterId: string) {
        deleteChapter(chapterId)
            .then(() => deleteCallback())
    }

    return <div className="chapter-preview">
            <div className="chapter-preview-title clickable" onClick={()=>handleOnClick()}>{t("book.chapter")} {chapter.position}: {chapter.title}</div>
            { isOwner ? <button onClick={() => navigate(`/book/${bookId}/chapter/${chapter.position}/edit`)}>{t("book.edit")}</button> : null }
            {chapter.description.length != 0 ? <div className="chapter-preview-description">{t("book.chapter-description")}: {chapter.description}</div>: <div></div>}
            { isOwner ? <button onClick={() => handleDeleteChapter(chapter.id)}>{t("book.delete-chapter")}</button> : null }
        </div>
}