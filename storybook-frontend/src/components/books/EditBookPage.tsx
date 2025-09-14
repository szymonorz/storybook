import BookResponse, { createBook, getBookInfo, patchBook } from "../../utils/api/book"
import { useTranslation } from "react-i18next"
import { useNavigate, useParams } from "react-router"
import { useEffect, useState } from "react"
import BookForm from "./BookForm"
import { getCurrentUser } from "../../utils/api/user"


interface CreateBookFormValues {
    title: string,
    description: string
}

export default function EditBookPage() {
    
    const {t} = useTranslation()
    const navigate = useNavigate()
    const [bookState, setBookState] = useState<BookResponse>()
    const [error, setError] = useState<string>()
    const [owner, setIsOwner] = useState<boolean>()

    const {bookId} = useParams()

    useEffect(() => {
        const auth = localStorage.getItem("_auth_token")
        if(!auth) {
            navigate("/login?redirect=must_be_logged_in")
        }
    }, [navigate])

    useEffect(() => {
        if(bookId == null) return

        getBookInfo(bookId)
                .then((data) => setBookState(data))
                .catch((error) => setError(error))

    }, [bookId])

    useEffect(() => {
        getCurrentUser()
            .then((data) => {
                if(data.id == bookState?.author.id) {
                    setIsOwner(true)
                }
            })
            .catch(() => setIsOwner(false))
    }, [bookState])

    useEffect(() => {
        if(owner === false) {
            navigate("/403")
        }
    }, [owner])

    function onSubmit(data: CreateBookFormValues) {
        if(bookId) {
            patchBook(bookId, data)
                .then((resp) => {
                    navigate(`/book/${resp.id}`)
                })
        } else {
            setError(t("edit_book.id_not_provided"))
        }
    }

    return (
        <div className="page">
            {error ? <div>{error}</div> : null}
            <div className="main-component">
                <BookForm onSubmit={onSubmit} formValues={{
                    title: bookState?.title ?? "",
                    description: bookState?.description ?? ""
                }}/>
            </div>
        </div>
    )
}