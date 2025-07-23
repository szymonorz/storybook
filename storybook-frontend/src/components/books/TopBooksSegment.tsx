import { useEffect, useState } from "react"
import BookResponse, { getLatestBooks } from "../../utils/api/book"
import BookList from "./BookList"

export default function TopBooksSegment() {
    const [books, setBooks] = useState<BookResponse[]>([])

    useEffect(() => {
        getLatestBooks()
            .then((data) => setBooks(data))
    }, [])
    
    return (
        <div className="top-books-segment">
            <BookList books={books}/>
        </div>
    )
}