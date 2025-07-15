import ShortChapterResponse from "./chapter"
import { config } from "../config"
import UserResponse from "./user"

interface BookResponse {
    id: string,
    title: string,
    description: string
    author: UserResponse,
    chapters: ShortChapterResponse[]
}

interface CreateBookRequest {
    title: string,
    description: string,
    tags: string[],
    keywords: string[]
}

export async function createBook(req: CreateBookRequest): Promise<BookResponse> {
    const token = localStorage.getItem("_auth_token")
    const response = await fetch(`${config.url}/api/book`, {
        method: "POST",
        body: JSON.stringify(req),
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    }) 

    return response.json()
}

export async function getCurrentUserBooks() {
    const token = localStorage.getItem("_auth_token")
    const response = await fetch(`${config.url}/api/currentuser/books`, {
        method: "GET",
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    }) 

    return response.json()
}

export async function getBookInfo(bookId: string): Promise<BookResponse> {
    const token = localStorage.getItem("_auth_token")
    const response = await fetch(`${config.url}/api/book/${bookId}`, {
        method: "GET",
        headers: token ? new Headers({"Authorization": `Bearer ${token}`}): new Headers()
    })

    return response.json()
}


export function getTopBooks(): BookResponse[] {
    //TODO: axios api call

    return [
        {
            id: "aaaa", title: "AAAAAA", description: "AAAAAAAAAAA",
            author: { id: "xxxx", username: "xxxxxx" },
            chapters: []
        },
        {
            id: "bbbb", title: "BBBBBBB", description: "AAAAAAAAAAA",
            author: { id: "xxxx", username: "xxxxxx" },
            chapters: []
        },
        {
            id: "ccccc", title: "CCCCCCC", description: "AAAAAAAAAAA",
            author: { id: "xxxx", username: "xxxxxx" },
            chapters: []
        },
        {
            id: "dddd", title: "DDDDDD", description: "AAAAAAAAAAA",
            author: { id: "xxxx", username: "xxxxxx" },
            chapters: []
        },
        {
            id: "ddqdd", title: "DDDDDD", description: "AAAAAAAAAAA",
            author: { id: "xxxx", username: "xxxxxx" },
            chapters: []
        },
        {
            id: "eeee", title: "DDDDDD", description: "AAAAAAAAAAA",
            author: { id: "xxxx", username: "xxxxxx" },
            chapters: []
        },
        {
            id: "fffff", title: "DDDDDD", description: "AAAAAAAAAAA",
            author: { id: "xxxx", username: "xxxxxx" },
            chapters: []
        },
    ]
}


export default BookResponse