import { useNavigate } from "react-router";
import BookResponse from "../../utils/api/book";
import { useTranslation } from "react-i18next";

interface BookPreviewProps {
    book: BookResponse
}

export default function BookPreview({book}: BookPreviewProps) {
    const navigate = useNavigate()
    const {t} = useTranslation()

    function navigateToBook(id: string) {
        navigate(`/book/${id}`)
    }

    function navigateToAuthorBooks(authorId: string) {
        navigate(`/books?${new URLSearchParams({userId: authorId})}`)
    }

    return (
        <div className="book-preview">
            <h3 onClick={() => navigateToBook(book.id)}  className="clickable">{book.title}</h3>
            <label>
                <span onClick={() => navigateToAuthorBooks(book.author.id)} className="book-preview-author">{t("book.author")}: <b>{book.author.username}</b></span>
            </label>
            <hr/>
            <label>{t("book.description")}: </label>
            <div>{book.description}</div>
        </div>
    )
}