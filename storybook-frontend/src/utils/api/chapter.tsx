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

export interface CreateOrPatchBookRequest {
    bookId: string,
    chapterTitle: string,
    chapterDescription: string,
    authorsNote: string,
    chapterContent: string
}


export async function createChapter(request: CreateOrPatchBookRequest): Promise<ShortChapterResponse> {
    const token = localStorage.getItem('_auth_token')
    const response = await fetch(`${config.url}/api/book/chapter`, {
        method: "POST",
        body: JSON.stringify(request),
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    })

    return response.json()
}

export async function patchChapter(chapterId: string, request: CreateOrPatchBookRequest): Promise<ShortChapterResponse> {
    const token = localStorage.getItem('_auth_token')
    const response = await fetch(`${config.url}/api/chapter/${chapterId}`, {
        method: "PATCH",
        body: JSON.stringify(request),
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    })

    return response.json()
}

export async function deleteChapter(chapterId: string): Promise<string> {
    const token = localStorage.getItem('_auth_token')
    const response = await fetch(`${config.url}/api/chapter/${chapterId}`, {
        method: "DELETE",
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    })

    return response.text()
}

export async function getChapter(bookId: string, chapterNumber: string): Promise<ChapterContentResponse> {
    const token = localStorage.getItem('_auth_token')
    const response = await fetch(`${config.url}/api/book/${bookId}/chapter/${chapterNumber}`, {
        method: "GET",
        headers: new Headers({"Authorization": `Bearer ${token}`})
    })

    return response.json()
}

export default ShortChapterResponse