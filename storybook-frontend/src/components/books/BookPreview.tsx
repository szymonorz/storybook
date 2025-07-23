import { useNavigate } from "react-router";
import BookResponse from "../../utils/api/book";

interface BookPreviewProps {
    book: BookResponse
}

export default function BookPreview({book}: BookPreviewProps) {
    const navigate = useNavigate()
    function navigateToBook(id: string) {
        navigate(`/book/${id}`)
    }

    return (
        <div className="book-preview" onClick={() => navigateToBook(book.id)}>
            <h3 className="clickable">{book.title}</h3>
            <label>
                <span className="book-preview-author">Author: {book.author.username}</span>
            </label>
            <hr/>
            <label>Description: </label>
            <div>{book.description}</div>
        </div>
    )
}