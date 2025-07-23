import { config } from "../config"

interface UserResponse {
    id: string,
    username: string
}

interface CreateUserRequest {
    email: string,
    username: string,
    password: string
}

interface LoginRequest {
    email: string,
    password: string
}

export async function registerUser(userRequest: CreateUserRequest) {
    console.log(userRequest)

    const response = await fetch(`${config.url}/api/user/register`, {
        method: "POST",
        body: JSON.stringify(userRequest),
        headers: new Headers({"Content-Type": "application/json"})
    })

    // if(!response.ok) {
    //     console.log("NOT OK")
    //     return new Error("Failed to create user")
    // }
    console.log("xxx")
    return response.json()
}

export async function loginUser(loginRequest: LoginRequest) {
    console.log(loginRequest)

    const response = await fetch(`${config.url}/auth`, {
        method: "POST",
        body: JSON.stringify(loginRequest)
    })

    if(!response.ok) {
        return new Error("Failed to log in")
    }

    const token_pair = await response.json()
    console.log(token_pair)

    localStorage.setItem("_auth_token", token_pair.access_token)
}

export async function getCurrentUser(token: string) {
    const response = await fetch(`${config.url}/api/currentuser`, {
        method: "GET",
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    })

    return response.json()
}

export default UserResponse
