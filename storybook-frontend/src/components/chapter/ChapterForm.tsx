import { useForm } from "react-hook-form"
import { useTranslation } from "react-i18next"

interface ChapterFormProps {
    onSubmit: (p: ChapterFormValues) => void,
    bookId?: string,
    formValues?: ChapterFormValues
}


export interface ChapterFormValues {
    chapterTitle: string,
    chapterDescription: string,
    authorsNote: string,
    chapterContent: string
}

export default function ChapterForm({onSubmit, formValues}: ChapterFormProps) {
    const {register, handleSubmit} = useForm<ChapterFormValues>({
        values: formValues ?? {chapterTitle: "", chapterDescription: "", authorsNote: "", chapterContent: ""}
    })
    const {t} = useTranslation()

    return (
        <form className="create-form chapter-form" onSubmit={handleSubmit(onSubmit)}>
                    <input placeholder={t("chapter-form.placeholder.title")} type="text" {...register("chapterTitle")}/>
                    <textarea className="authors-note" placeholder={t("chapter-form.placeholder.authors-note")} {...register("authorsNote")}/>
                    <textarea className="description" placeholder={t("chapter-form.placeholder.description")} {...register("chapterDescription")}/>
                    <textarea placeholder="..." className="chapter-content" {...register("chapterContent")}/>
                    <button type="submit">{t("chapter-form.submit")}</button>
        </form>
    )
}