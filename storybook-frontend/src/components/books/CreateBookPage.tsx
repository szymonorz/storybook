import { useContext, useState, useEffect, useLayoutEffect } from "react"
import { AuthContext } from "../auth/AuthProvider"
import { useNavigate } from "react-router"
import { useForm } from "react-hook-form"
import { createBook } from "../../utils/api/book"

interface CreateBookFormValues {
    title: string,
    description: string,
    tags: string[],
    keywords: string[]
}

export default function CreateBookPage() {
    const {auth} = useContext(AuthContext)
    const {register, handleSubmit} = useForm<CreateBookFormValues>()
    const navigate = useNavigate()
    const [userNotFoundErr, setUserNotFoundErr] = useState<Boolean>(false)
    useEffect(() => {
        // if(auth == null) return 
        if(auth){
            setUserNotFoundErr(false)
        } else {
            setUserNotFoundErr(true)
            console.log("why ma i here")
            console.log(auth)
            navigate("/notLoggedIn")
        }
    }, [auth])

    function onSubmit(data: CreateBookFormValues) {
        data.tags = []
        data.keywords = []
        console.log(data)
        createBook(auth, data)
    }

    return (
        <div className="page">
            <div className="main-component">
                <form className="create-book-form" onSubmit={handleSubmit(onSubmit)}>
                    <input type="text" {...register("title")}/>
                    <textarea className="textarea" {...register("description")}/>

                    <button type="submit" value={"Submit"}/>
                </form>
            </div>
        </div>
    )
}