import { useNavigate, useParams } from "react-router"
import { createChapter, CreateOrPatchBookRequest } from "../../utils/api/chapter"
import { useEffect } from "react"
import ChapterForm, { ChapterFormValues } from "./ChapterForm"

export default function CreateChapterPage() {
    const navigate = useNavigate()
    const {bookId} = useParams()

    useEffect(() => {
        const auth = localStorage.getItem("_auth_token")
        if(!auth) {
            navigate("/login?redirect=must_be_logged_in")
        }
    }, [])

    function onSubmit(data: ChapterFormValues) {
    
        const reqData: CreateOrPatchBookRequest = {
            bookId: bookId!,
            ...data
        }
        
        createChapter(reqData)
            .then((data) => {
                navigate(`/book/${bookId}/chapter/${data.position}`)
            })
    }

    return (
        <div className="page">
            <div className="main-component">
                <ChapterForm onSubmit={onSubmit}/>
            </div>
        </div>
    )
}