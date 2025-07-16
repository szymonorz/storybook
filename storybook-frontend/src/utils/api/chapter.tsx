import { config } from "../config"

export interface ChapterContentResponse {
    id: string,
    title: string,
    description: string,
    authorsNote: string,
    content: string,
    position: number
}

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


export async function createChapter(request: NewBookChapterRequest): Promise<ShortChapterResponse> {
    const token = localStorage.getItem('_auth_token')
    const response = await fetch(`${config.url}/api/book/chapter`, {
        method: "POST",
        body: JSON.stringify(request),
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    })

    return response.json()
}

export async function getChapter(bookId: string, chapterId: string): Promise<ChapterContentResponse> {
    const token = localStorage.getItem('_auth_token')
    const response = await fetch(`${config.url}/api/book/${bookId}/chapter/${chapterId}`, {
        method: "GET",
        headers: new Headers({"Authorization": `Bearer ${token}`})
    })

    return response.json()
}

export default ShortChapterResponse