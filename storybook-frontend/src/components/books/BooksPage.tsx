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
        if(auth == null) return 
        const userId = searchParams.get("userId")
        if(userId) {
            setUserNotFoundErr(false)
        } else if(auth){
            setUserNotFoundErr(false)
            getCurrentUserBooks(auth)
                .then((data) => {
                    console.log(data)
                    setBooks(data)
                })
                .catch((err) => console.log(err))

        } else {
            setUserNotFoundErr(true)
            navigate("/notLoggedIn")
        }
    }, [auth, searchParams])

    return (
        <div className="page">
            {books.map((book) => <BookPreview key={book.id} book={book}/>)}
        </div>
    )
}