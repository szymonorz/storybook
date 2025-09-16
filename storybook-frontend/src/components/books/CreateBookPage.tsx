import { createBook } from "../../utils/api/book"
import { useNavigate } from "react-router"
import { useEffect } from "react"
import BookForm from "./BookForm"


interface CreateBookFormValues {
    title: string,
    description: string
}

export default function CreateBookPage() {
    const navigate = useNavigate()

    useEffect(() => {
        const auth = localStorage.getItem("_auth_token")
        if(!auth) {
            navigate("/login?redirect=must_be_logged_in")
        }
    }, [])

    function onSubmit(data: CreateBookFormValues) {
        createBook(data)
            .then((resp) => {
                navigate(`/book/${resp.id}`)
            })
    }

    return (
        <div className="page">
            <div className="main-component">
                <BookForm onSubmit={onSubmit}/>
            </div>
        </div>
    )
}