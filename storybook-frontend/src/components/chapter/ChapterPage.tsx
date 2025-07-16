import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router"
import { ChapterContentResponse, getChapter } from "../../utils/api/chapter"
import { useTranslation } from "react-i18next"

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
            
    }, [])

    return (
        <div className="page">
            <div className="main-component">
            {/* {error ?? (<div>{error}</div>)} */}
            {error  ? 
                (<div>Chapter not found</div>) 
                : 
                (<div className="book">
                    <h1>{t("chapter-page.chapter")} {chapterNumber}: {chapter?.title}</h1>
                    <h3>{t("book.author")}: {chapter?.authorsNote}</h3>
                    <p>{chapter?.description}</p>
                    <pre>{chapter?.content}</pre>
                    {/* <button onClick={() => navigate(`/book/${bookId}/createChapter`)}>{t("book.new_chapter")}</button> */}
                </div>)}
            </div>
        </div>
    )
}