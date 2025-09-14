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

interface CreateOrPatchBookRequest {
    title: string,
    description: string
}

export async function createBook(req: CreateOrPatchBookRequest): Promise<BookResponse> {
    const token = localStorage.getItem("_auth_token")
    const response = await fetch(`${config.url}/api/book`, {
        method: "POST",
        body: JSON.stringify(req),
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    }) 

    return response.json()
}

export async function patchBook(bookId: string, req: CreateOrPatchBookRequest): Promise<BookResponse> {
    const token = localStorage.getItem("_auth_token")
    const response = await fetch(`${config.url}/api/book/${bookId}`, {
        method: "PATCH",
        body: JSON.stringify(req),
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    }) 

    return response.json()
}

export async function getUserBooks(userId: string) {
    const token = localStorage.getItem("_auth_token")
    const response = await fetch(`${config.url}/api/user/${userId}/books`, {
        method: "GET",
        headers: token ? new Headers({"Authorization": `Bearer ${token}`}): new Headers()
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

export async function getLatestBooks(): Promise<BookResponse[]> {
    const token = localStorage.getItem("_auth_token")
    const response = await fetch(`${config.url}/api/book/latest?n=10`, {
        method: "GET",
        headers: token ? new Headers({"Authorization": `Bearer ${token}`}): new Headers()
    })

    return response.json()
}


export default BookResponse