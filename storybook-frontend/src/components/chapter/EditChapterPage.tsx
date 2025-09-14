import { useNavigate, useParams } from "react-router"
import { getChapter, CreateOrPatchBookRequest, patchChapter } from "../../utils/api/chapter"
import { useEffect, useState } from "react"
import ChapterForm, { ChapterFormValues } from "./ChapterForm"

export default function EditChapterPage() {
    const navigate = useNavigate()
    const {bookId, chapterNumber} = useParams()

    const [chapterId, setChapterId] = useState<string>()
    const [formValues, setFormValues] = useState<ChapterFormValues>()
    const [error, setError] = useState<string>()

    useEffect(() => {
        const auth = localStorage.getItem("_auth_token")
        if(!auth) {
            navigate("/login?redirect=must_be_logged_in")
        }
    }, [navigate])

    useEffect(() => {
        if(!bookId || !chapterNumber) return

        getChapter(bookId, chapterNumber)
            .then((response) => {
                console.log("hello")
                setChapterId(response.id)
                setFormValues({
                    chapterTitle: response.title,
                    chapterDescription: response.description,
                    authorsNote: response.authorsNote,
                    chapterContent: response.content
                })
            })

    }, [bookId, chapterNumber])

    function onSubmit(data: ChapterFormValues) {
        if(!bookId || !chapterId) return

        const reqData: CreateOrPatchBookRequest = {
            bookId: bookId,
            ...data
        }
        
        patchChapter(chapterId, reqData)
            .then((data) => {
                navigate(`/book/${bookId}/chapter/${data.position}`)
            })
            .catch((err) => setError(err))
    }

    return (
        <div className="page">
            <div className="main-component">
                <ChapterForm onSubmit={onSubmit} formValues={formValues}/>
            </div>
        </div>
    )
}