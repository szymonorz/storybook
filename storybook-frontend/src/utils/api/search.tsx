import { config } from "../config";
import BookResponse from "./book";

export default async function searchBooks(_q: string): Promise<BookResponse[]> {
    const token = localStorage.getItem("_auth_token")
    const response = await fetch(`${config.url}/api/book/search?${new URLSearchParams({q: _q})}`, {
        method: "GET",
        headers: token ? new Headers({"Authorization": `Bearer ${token}`}): new Headers()
    })

    return response.json()
}