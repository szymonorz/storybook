import ShortChapterResponse from "./chapter"
import UserResponse from "./user"

interface BookResponse {
    id: string,
    title: string,
    description: string
    author: UserResponse,
    chapters: ShortChapterResponse[]
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
            id: "dddd", title: "DDDDDD", description: "AAAAAAAAAAA",
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