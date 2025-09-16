import { useEffect } from "react";
import BookResponse from "../../utils/api/book";
import BookPreview from "./BookPreview";

interface BookListProps {
    books: BookResponse[],
    isOwner?: boolean,
    deleteCallback?: () => void
}

export default function BookList({books, isOwner, deleteCallback}: BookListProps) {
    return <div className="book-list">
        {books.map((book) => 
            <BookPreview 
                key={book.id} 
                book={book}
                isOwner={isOwner}
                deleteCallback={deleteCallback}    
            />)}
    </div>
}