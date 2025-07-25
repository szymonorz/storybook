import { useTranslation } from "react-i18next";
import ShortChapterResponse from "../../utils/api/chapter";
import { useNavigate, useParams } from "react-router";

interface ChapterPreviewProps {
    chapter: ShortChapterResponse
}

export default function ChapterPreview({chapter}: ChapterPreviewProps) {
    const {t} = useTranslation()
    const {bookId} = useParams()
    const navigate = useNavigate()

    function handleOnClick() {
        navigate(`/book/${bookId}/chapter/${chapter.position}`)
    }

    return <div className="chapter-preview" onClick={()=>handleOnClick()}>
            <div className="chapter-preview-title clickable">{t("book.chapter")} {chapter.position}: {chapter.title}</div>
            {chapter.description.length != 0 ? <div className="chapter-preview-description">{t("book.chapter-description")}: {chapter.description}</div>: <div></div>}
        </div>
}