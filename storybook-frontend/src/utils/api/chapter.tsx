import { config } from "../config"

export interface ShortChapterResponse {
    id: string,
    title: string,
    description: string,
    position: number
}

export interface NewBookChapterRequest {
    bookId: string,
    chapterTitle: string,
    chapterDescription: string,
    authorNote: string,
    chapterContent: string
}


export async function createChapter(request: NewBookChapterRequest) {
    const token = localStorage.getItem('_auth_token')
    const response = await fetch(`${config.url}/api/book/chapter`, {
        method: "POST",
        body: JSON.stringify(request),
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    })

    return response.json()
}

export default ShortChapterResponse