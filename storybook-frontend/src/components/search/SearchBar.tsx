import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router";

interface SearchBarFormValues {
    query: string
}

export default function SearchBar() {
    const [query, setQueryState] = useState<string>()
    const navigate = useNavigate()
    const {register, handleSubmit} = useForm<SearchBarFormValues>()

    function onSubmit() {
        if(query) {
            navigate(`/search?${new URLSearchParams({q: query})}`)
        }
    }

    function handleChange(event: any) {
        setQueryState(event.target.value)
    }

    return <div>
            <form onSubmit={handleSubmit(onSubmit)}>
                <input type="text"
                 {...register("query", {
                    onChange: handleChange
                })}/>
            </form>
    </div>
}