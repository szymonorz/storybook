import { useEffect, useState } from "react"
import { useNavigate, useSearchParams } from "react-router"
import BookResponse, { getCurrentUserBooks } from "../../utils/api/book"
import { useTranslation } from "react-i18next"
import BookList from "./BookList"

export default function AuthorBooksPage() {
    const [searchParams] = useSearchParams()
    const navigate = useNavigate()
    const [userNotFoundErr, setUserNotFoundErr] = useState<Boolean>(false)
    const {t} = useTranslation()
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
    }, [searchParams])

    return (
        <div className="page">
            <div className="main-component">
                { userNotFoundErr ? 
                    <>{t("books.user-not-found")}</> 
                        :
                    ( 
                        <>
                            <BookList books={books}/>
                            <button onClick={() => navigate("/createBook")}>{t("books.create-book")}</button>
                        </>
                    )
                }
            </div>
        </div>
    )
}