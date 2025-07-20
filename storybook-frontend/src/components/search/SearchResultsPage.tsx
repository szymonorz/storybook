import { useEffect, useState } from "react";
import BookList from "../books/BookList";
import BookResponse from "../../utils/api/book";
import { useSearchParams } from "react-router";
import searchBooks from "../../utils/api/search";
import { useTranslation } from "react-i18next";

export default function SearchResultsPage() {
    const [books, setBooksState] = useState<BookResponse[]>()
    const [query, setQueryState] = useState<string>()
    const [error, setError] = useState()
    const [searchParams] = useSearchParams()
    const {t} = useTranslation()
    useEffect(() => {
        const q = searchParams.get("q")
        
        if(q) {
            setQueryState(q)
            searchBooks(q)
                .then((data) => setBooksState(data))
                .catch((err) => setError(err))
        }

    }, [searchParams])

   return (
        <div className="page">
            <div className="main-component">
                <h1>{t("search-page.results")}: {query}</h1>
               { books && !error ? <BookList books={books}/> : <div></div> }
            </div>
        </div>
   ) 
}