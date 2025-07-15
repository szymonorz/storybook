import { useContext, useState, useEffect, useLayoutEffect } from "react"
import { AuthContext } from "../auth/AuthProvider"
import { useNavigate, useParams } from "react-router"
import { useForm } from "react-hook-form"
import { createBook } from "../../utils/api/book"
import { createChapter, NewBookChapterRequest } from "../../utils/api/chapter"

interface CreateChapterFormValues {
    chapterTitle: string,
    chapterDescription: string,
    authorNote: string,
    chapterContent: string
}

export default function CreateChapterPage() {

    const {register, handleSubmit} = useForm<CreateChapterFormValues>()
    const navigate = useNavigate()
    const [userNotFoundErr, setUserNotFoundErr] = useState<Boolean>(false)
    const {bookId} = useParams()

    useEffect(() => {
        console.log(bookId)
    }, [bookId])

    function onSubmit(data: CreateChapterFormValues) {
    
        // console.log(data)
        const reqData: NewBookChapterRequest = {
            bookId: bookId!,
            ...data
        }
        
        console.log(reqData)
        createChapter(reqData)
        // createBook(auth, data)
    }

    return (
        <div className="page">
            <div className="main-component">
                <form className="create-form chapter-form" onSubmit={handleSubmit(onSubmit)}>
                    <input placeholder="Title" type="text" {...register("chapterTitle")}/>
                    <textarea className="authors-note" placeholder="Authors note (optional)" {...register("authorNote")}/>
                    <textarea className="authors-note" placeholder="Authors note (optional)" {...register("chapterDescription")}/>
                    <textarea placeholder="..." className="textarea" {...register("chapterContent")}/>
                    <button type="submit" value={"Submit"}/>
                </form>
            </div>
        </div>
    )
}