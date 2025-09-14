import { useEffect } from "react"
import { useForm } from "react-hook-form"
import { useTranslation } from "react-i18next"

interface BookFormProps {
    onSubmit: (p: BookFormValues) => void,
    formValues?: BookFormValues
}

interface BookFormValues {
    title: string,
    description: string
}

export default function BookForm({onSubmit, formValues}: BookFormProps) {
    const {t} = useTranslation()
    const {register, handleSubmit, reset} = useForm<BookFormValues>({
        values: formValues ?? {title: "", description: ""}
    })

    useEffect(() => {
        if (formValues) reset(formValues); 
      }, [formValues, reset]);
    
    return  <form className="create-form book-form" onSubmit={handleSubmit(onSubmit)}>
                <label>{t("create-book.title")}</label>
                <input className="title" type="text" {...register("title")}/>
                <label>{t("create-book.description")}</label>
                <textarea className="description" {...register("description")}/>
                <button type="submit">{t("create-book.submit")}</button>
            </form>
}