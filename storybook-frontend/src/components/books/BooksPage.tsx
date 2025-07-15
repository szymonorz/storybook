import { useContext, useEffect, useState } from "react"
import { AuthContext } from "../auth/AuthProvider"
import { useLocation, useNavigate, useSearchParams } from "react-router"
import BookResponse, { getCurrentUserBooks } from "../../utils/api/book"
import BookPreview from "./BookPreview"

export default function BooksPage() {
    const { auth } = useContext(AuthContext)
    const [searchParams, setSearchParams] = useSearchParams()
    const navigate = useNavigate()
    const [userNotFoundErr, setUserNotFoundErr] = useState<Boolean>(false)
    const [books, setBooks] = useState<BookResponse[]>([])

    useEffect(() => {
        const userId = searchParams.get("userId")
        if(userId) {
            setUserNotFoundErr(false)
        } else {
            setUserNotFoundErr(false)
            getCurrentUserBooks()
                .then((data) => {
                    console.log(data)
                    setBooks(data)
                })
                .catch((err) => console.log(err))

        }
    }, [auth, searchParams])

    return (
        <div className="page">
            <div className="main-component">
                {books.map((book) => <BookPreview key={book.id} book={book}/>)}
            </div>
        </div>
    )
}