import { useNavigate } from "react-router";
import BookResponse, { deleteBook } from "../../utils/api/book";
import { useTranslation } from "react-i18next";
import formatDate from "../../utils/date";

interface BookPreviewProps {
    book: BookResponse,
    isOwner?: boolean,
    deleteCallback?: () => void
}

export default function BookPreview({book, isOwner, deleteCallback}: BookPreviewProps) {
    const navigate = useNavigate()
    const {t} = useTranslation()

    function navigateToBook(id: string) {
        navigate(`/book/${id}`)
    }

    function navigateToAuthorBooks(authorId: string) {
        navigate(`/books?${new URLSearchParams({userId: authorId})}`)
    }

    function handleDeleteBook(bookId: string) {
        deleteBook(bookId)
            .then(() => deleteCallback!())
    }

    return (
        <div className="book-preview">
            <h3 onClick={() => navigateToBook(book.id)}  className="clickable">{book.title}</h3>
            <div className="inline">
            <label>
                <span onClick={() => navigateToAuthorBooks(book.author.id)} className="book-preview-author">{t("book.author")}: <b>{book.author.username}</b></span>
            </label>
            <div></div>
                { isOwner ? <button onClick={() => handleDeleteBook(book.id)}>{t("book-preview.delete-book")}</button> : null }
            </div>
            <hr/>
            <label>{t("book.description")}: </label>
            <div>{book.description}</div>
            <div className="book-last-updated">
                <label>{t("book.last-update")}</label>
                <div>{formatDate(book.updatedAt)}</div>
            </div>
        </div>
    )
}