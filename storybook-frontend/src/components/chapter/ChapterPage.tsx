import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router"
import { ChapterContentResponse, getChapter } from "../../utils/api/chapter"
import { useTranslation } from "react-i18next"
import ChapterSlider from "./ChapterSlider"

export default function ChapterPage() {
    
    const {bookId, chapterNumber} = useParams()
    const [chapter, setChapter] = useState<ChapterContentResponse | null>(null)
    const [error, setError] = useState<string>()
    const {t} = useTranslation()
    const navigate = useNavigate()

    useEffect(() => {
        console.log("Hello?")
        console.log
        if (!bookId || !chapterNumber) {
            setError("No chapter or book found")
            return
        } 
        
        getChapter(bookId, chapterNumber)
            .then((data) => setChapter(data))
            .catch((error) => setError(error))
            
    }, [bookId, chapterNumber])

    function chapterList() {
        navigate(`/book/${bookId}`)
    }

    return (
        <div className="page">
            <div className="main-component">
            {error  ? 
                (<div>Chapter not found</div>) 
                : 
                (<div className="chapter">
                    <div className="chapter-list" onClick={() => chapterList()}>{t("chapter.back")}</div>
                    <ChapterSlider bookId={bookId} chapterNumber={Number(chapterNumber)}/>
                    <h1>{t("chapter-page.chapter")} {chapterNumber}: {chapter?.title}</h1>
                    {chapter?.authorsNote != undefined && chapter.authorsNote.length != 0 ? <h3>{t("chapter.authors-note")}: {chapter?.authorsNote}</h3>: <h3></h3>}
                    <label htmlFor="description">{t("chapter-page.description")}:</label>
                    <div className="description">{chapter?.description}</div>
                    <pre className="content">{chapter?.content}</pre>
                    <ChapterSlider bookId={bookId} chapterNumber={Number(chapterNumber)}/>
                </div>)}
            </div>
        </div>
    )
}