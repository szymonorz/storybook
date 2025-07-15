import { useEffect, useState, useContext } from "react"
import { useNavigate, useParams } from "react-router"
import { AuthContext } from "../auth/AuthProvider"
import { useTranslation } from "react-i18next"
import BookResponse, { getBookInfo, getBookInfoUnauth } from "../../utils/api/book"
import ChapterPreview from "../chapter/ChapterPreview"

interface BookProps {
    bookId: string
}

export default function BookPage() {

    const {auth} = useContext(AuthContext)
    const navigate = useNavigate()
    const [userNotFoundErr, setUserNotFoundErr] = useState<Boolean>(false)
    const {t} = useTranslation()

    const {bookId} = useParams()

    const [error, setError] = useState<string | null>(null)
    const [bookState, setBookState] = useState<BookResponse | null>(null)

    useEffect(() => {
        if(bookId == null) return

        getBookInfo(bookId)
                .then((data) => setBookState(data))

    }, [bookId])

    return <div className="page">
        <div className="main-component">
            {bookId == null ? 
                (<div>Book not found</div>) 
                : 
                (<div>
                    <h1>{bookState?.title}</h1>
                    <h3>{t("book.author")}: {bookState?.author.username}</h3>
                    <p>{bookState?.description}</p>
                    {bookState?.chapters.map((chapter) => (<ChapterPreview chapter={chapter}/>) )}
                    <button onClick={() => navigate(`/book/${bookId}/createChapter`)}>{t("book.new_chapter")}</button>
                
                </div>)}
        </div>
    </div>
}