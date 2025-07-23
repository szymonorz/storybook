import { useEffect, useState } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router"
import { getBookInfo } from "../../utils/api/book"

interface ChapterSliderProps {
    chapterNumber?: number,
    bookId?: string
}

export default function ChapterSlider({bookId, chapterNumber}: ChapterSliderProps) {
    const {t} = useTranslation()
    const navigate = useNavigate()
    const [chapterCount, setChapterCount] = useState<number>(0)
    function previousChapter() {
        navigate(`/book/${bookId}/chapter/${Number(chapterNumber) - 1}`)
    }

    function nextChapter() {
        navigate(`/book/${bookId}/chapter/${Number(chapterNumber) + 1}`)
    }

    useEffect(() => {
        getBookInfo(bookId!)
            .then((data) => setChapterCount(data.chapters.length))
    }, [])

    return (
        <div className="chapter-slider">
            {chapterNumber != chapterCount ? <div className="clickable" onClick={nextChapter}>{t("chapter.next")}</div>: <div></div>}
            {chapterNumber != 1 ? <div className="clickable" onClick={previousChapter}>{t("chapter.previous")}</div>: <div></div>}
        </div>
    )
}