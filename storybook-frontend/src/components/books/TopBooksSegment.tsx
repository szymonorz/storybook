import { useEffect, useState } from "react"
import BookResponse, { getTopBooks } from "../../utils/api/book"
import BookPreview from "./BookPreview"

export default function TopBooksSegment() {
    const [books, setBooks] = useState<BookResponse[]>([])

    useEffect(() => {
        let _books = getTopBooks()
        setBooks(_books)
    }, [])
    
    return (
        <div className="top-books-segment">
            {books.map(book => <BookPreview key={book.id} book={book}/>)}
        </div>
    )
}