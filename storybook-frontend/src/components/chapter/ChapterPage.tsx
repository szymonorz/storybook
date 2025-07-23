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
                    <div className="chapter-list clickable" onClick={() => chapterList()}>{t("chapter.back")}</div>
                    <ChapterSlider bookId={bookId} chapterNumber={Number(chapterNumber)}/>
                    <h1>{t("chapter-page.chapter")} {chapterNumber}: {chapter?.title}</h1>
                    {chapter?.authorNote != undefined && chapter.authorNote.length != 0 ? <pre>{t("chapter-page.authors-note")}: {chapter?.authorNote}</pre>: <h3></h3>}
                    {chapter?.description != undefined && chapter.description.length != 0 ? 
                    (   <div>                 
                            <label htmlFor="description">{t("chapter-page.description")}:</label>
                            <pre className="description">{chapter?.description}</pre>
                        </div>
                        )
                    : <h3></h3>}

                    <pre className="content">{chapter?.content}</pre>
                    <ChapterSlider bookId={bookId} chapterNumber={Number(chapterNumber)}/>
                </div>)}
            </div>
        </div>
    )
}