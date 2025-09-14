import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router"
import { useTranslation } from "react-i18next"
import BookResponse, { getBookInfo } from "../../utils/api/book"
import ChapterPreview from "../chapter/ChapterPreview"
import { getCurrentUser } from "../../utils/api/user"

export default function BookPage() {

    const navigate = useNavigate()
    const {t} = useTranslation()

    const {bookId} = useParams()

    const [error, setError] = useState<string | null>(null)
    const [bookState, setBookState] = useState<BookResponse | null>(null)
    const [isOwner, setIsOwner] = useState<boolean>(false)


    useEffect(() => {
        if(bookId == null) return

        getBookInfo(bookId)
                .then((data) => setBookState(data))
                .catch((error) => setError(error))

    }, [bookId])

    useEffect(() => {
        getCurrentUser()
            .then((data) => {
                if(data.id == bookState?.author.id) {
                    setIsOwner(true)
                }
            })
            .catch(() => setIsOwner(false))
    }, [bookState])

    return <div className="page">
        <div className="main-component">
            {bookId == null || error ? 
                (<div>Book not found</div>) 
                : 
                (<div className="book">
                    <button onClick={() => navigate(`/book/${bookId}/edit`)}>{t("book.edit")}</button>
                    <h1>{bookState?.title}</h1>
                    <h3>{t("book.author")}: {bookState?.author.username}</h3>
                    <label>{t("book.description")}</label>
                    <p>{bookState?.description}</p>
                    <div className="book-chapter-list">
                        <h5>{t("book.chapters")}:</h5>
                        {bookState?.chapters.map((chapter) => (<ChapterPreview key={chapter.id} chapter={chapter} isOwner={isOwner}/>) )}                    
                    </div>
                    { isOwner ? <button onClick={() => navigate(`/book/${bookId}/createChapter`)}>{t("book.new_chapter")}</button> : <></>}                
                </div>)}
        </div>
    </div>
}