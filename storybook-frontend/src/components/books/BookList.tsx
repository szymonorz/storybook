import BookResponse from "../../utils/api/book";
import BookPreview from "./BookPreview";

interface BookListProps {
    books: BookResponse[]
}

export default function BookList({books}: BookListProps) {
    return <div className="book-list">
        {books.map((book) => <BookPreview key={book.id} book={book}/>)}
    </div>
}