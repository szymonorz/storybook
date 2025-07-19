import { useEffect, useState } from "react"
import BookResponse, { getLatestBooks } from "../../utils/api/book"
import BookPreview from "./BookPreview"

export default function TopBooksSegment() {
    const [books, setBooks] = useState<BookResponse[]>([])

    useEffect(() => {
        getLatestBooks()
            .then((data) => setBooks(data))
    }, [])
    
    return (
        <div className="top-books-segment">
            {books.map(book => <BookPreview key={book.id} book={book}/>)}
        </div>
    )
}