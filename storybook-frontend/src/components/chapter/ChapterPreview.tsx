import { useTranslation } from "react-i18next";
import ShortChapterResponse from "../../utils/api/chapter";

interface ChapterPreviewProps {
    chapter: ShortChapterResponse
}

export default function ChapterPreview({chapter}: ChapterPreviewProps) {
    const {t} = useTranslation()
    return <div className="chapter-preview">
            <div className="chapter-preview-title">{t("book.chapter")} {chapter.position}: {chapter.title}</div>
            <div className="chapter-preview-description">{chapter.description}</div>
        </div>
}