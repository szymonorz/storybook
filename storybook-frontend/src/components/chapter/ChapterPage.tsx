import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router"
import { ChapterContentResponse, getChapter } from "../../utils/api/chapter"
import { useTranslation } from "react-i18next"
import ChapterSlider from "./ChapterSlider"
import formatDate from "../../utils/date"

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
                    <div className="inline">
                        <div className="chapter-list clickable" onClick={() => chapterList()}>{t("chapter.back")}</div>
                        <div></div>
                        <div className="book-times">
                            <div className="book-last-updated">
                                <label>{t("book.created-time")}</label>
                                { chapter?.createdAt != undefined ? <div>{formatDate(chapter?.createdAt)}</div> : null}
                            </div>
                            <hr></hr>
                            <div className="book-last-updated">
                                <label>{t("book.last-update")}</label>
                                { chapter?.updatedAt != undefined ? <div>{formatDate(chapter?.updatedAt)}</div> : null}
                            </div>
                        </div>
                    </div>
                    <ChapterSlider bookId={bookId} chapterNumber={Number(chapterNumber)}/>
                    <h1>{t("chapter-page.chapter")} {chapterNumber}: {chapter?.title}</h1>
                    {chapter?.authorsNote != undefined && chapter.authorsNote.length != 0 ? <pre>{t("chapter-page.authors-note")}: {chapter?.authorsNote}</pre>: <h3></h3>}
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