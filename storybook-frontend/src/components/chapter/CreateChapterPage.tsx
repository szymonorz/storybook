import { useNavigate, useParams } from "react-router"
import { useForm } from "react-hook-form"
import { createChapter, NewBookChapterRequest } from "../../utils/api/chapter"
import { useTranslation } from "react-i18next"

interface CreateChapterFormValues {
    chapterTitle: string,
    chapterDescription: string,
    authorNote: string,
    chapterContent: string
}

export default function CreateOrEditChapterPage() {

    const {register, handleSubmit} = useForm<CreateChapterFormValues>()
    const navigate = useNavigate()
    const {t} = useTranslation()
    const {bookId} = useParams()

    function onSubmit(data: CreateChapterFormValues) {
    
        // console.log(data)
        const reqData: NewBookChapterRequest = {
            bookId: bookId!,
            ...data
        }
        
        // console.log(reqData)
        createChapter(reqData)
            .then((data) => {
                navigate(`/book/${bookId}/chapter/${data.position}`)
            })
    }

    return (
        <div className="page">
            <div className="main-component">
                <form className="create-form chapter-form" onSubmit={handleSubmit(onSubmit)}>
                    <input placeholder={t("chapter-form.placeholder.title")} type="text" {...register("chapterTitle")}/>
                    <textarea className="authors-note" placeholder={t("chapter-form.placeholder.authors-note")} {...register("authorNote")}/>
                    <textarea className="description" placeholder={t("chapter-form.placeholder.description")} {...register("chapterDescription")}/>
                    <textarea placeholder="..." className="chapter-content" {...register("chapterContent")}/>
                    <button type="submit">{t("chapter-form.submit")}</button>
                </form>
            </div>
        </div>
    )
}