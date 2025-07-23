import { useEffect, useState } from "react"
import { useNavigate, useSearchParams } from "react-router"
import BookResponse, { getCurrentUserBooks, getUserBooks } from "../../utils/api/book"
import { useTranslation } from "react-i18next"
import BookList from "./BookList"
import UserResponse, { getUser } from "../../utils/api/user"

export default function AuthorBooksPage() {
    const [searchParams] = useSearchParams()
    const navigate = useNavigate()
    const [userNotFoundErr, setUserNotFoundErr] = useState<Boolean>(false)
    const [author, setAuthor] = useState<UserResponse>()
    const [isOwner, setIsOwner] = useState<boolean>(false)
    const {t} = useTranslation()
    const [books, setBooks] = useState<BookResponse[]>([])

    useEffect(() => {
        const userId = searchParams.get("userId")
        if(userId) {
            setIsOwner(false)
            getUser(userId)
                .then((data) => setAuthor(data))
                .catch((err) => {
                    setUserNotFoundErr(true)
                    console.log(err)
                    return
                })

            getUserBooks(userId)
                .then((data) => setBooks(data))
                .catch(() => setUserNotFoundErr(true))
            
        } else {
            setUserNotFoundErr(false)
            setIsOwner(true)
            getCurrentUserBooks()
                .then((data) => {
                    console.log(data)
                    setBooks(data)
                })
                .catch((err) => console.log(err))

        }
    }, [searchParams])

    return (
        <div className="page">
            <div className="main-component">
                { userNotFoundErr ? 
                    <>{t("books.user-not-found")}</> 
                        :
                    ( 
                        <>
                            {
                                isOwner ?
                                <h1>{t("books.your-books")}</h1>
                                :
                                <h1>{t("books.user-books", {user: author?.username})}</h1>
                            }
                            <BookList books={books}/>
                            { isOwner ? <button onClick={() => navigate("/createBook")}>{t("books.create-book")}</button> : <></>}
                        </>
                    )
                }
            </div>
        </div>
    )
}