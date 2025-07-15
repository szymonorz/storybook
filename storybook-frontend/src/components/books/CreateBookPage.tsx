import { useForm } from "react-hook-form"
import { createBook } from "../../utils/api/book"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router"


interface CreateBookFormValues {
    title: string,
    description: string,
    tags: string[],
    keywords: string[]
}

export default function CreateBookPage() {
    const {register, handleSubmit} = useForm<CreateBookFormValues>()
    const {t} = useTranslation()
    const navigate = useNavigate()

    function onSubmit(data: CreateBookFormValues) {
        data.tags = []
        data.keywords = []
        console.log(data)
        createBook(data)
            .then((resp) => {
                navigate(`/book/${resp.id}`)
            })
    }

    return (
        <div className="page">
            <div className="main-component">
                <form className="create-form book-form" onSubmit={handleSubmit(onSubmit)}>
                    <label>{t("create-book.title")}</label>
                    <input type="text" {...register("title")}/>
                    <label>{t("create-book.description")}</label>
                    <textarea className="textarea" {...register("description")}/>

                    <button type="submit">{t("create-book.submit")}</button>
                </form>
            </div>
        </div>
    )
}