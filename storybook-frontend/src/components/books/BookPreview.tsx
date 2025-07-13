import BookResponse from "../../utils/api/book";

interface BookPreviewProps {
    book: BookResponse
}

export default function BookPreview({book}: BookPreviewProps) {
    return (
        <div className="book-preview">
            <h3>{book.title}</h3>
            <label>
                <span className="book-preview-author">Author: {book.author.username}</span>
            </label>
            <hr/>
            <label>Description: </label>
            <div>{book.description}</div>
        </div>
    )
}