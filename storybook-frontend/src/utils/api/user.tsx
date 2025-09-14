import { config } from "../config"
import { UserRole } from "./role"

interface UserResponse {
    id: string,
    username: string,
    email: string,
    userRoles: UserRole[]
}

interface CreateUserRequest {
    email: string,
    username: string,
    password: string
}

interface LoginRequest {
    username: string,
    password: string
}

interface PatchUserRequest {
    username: string | null,
    email: string | null,
    password: string | null
}

interface TokenResponse {
    access_token: string,
    refresh_token: string
}

export async function registerUser(userRequest: CreateUserRequest) {
    const response = await fetch(`${config.url}/api/user/register`, {
        method: "POST",
        body: JSON.stringify(userRequest),
        headers: new Headers({"Content-Type": "application/json"})
    })

    return response.json()
}

export async function loginUser(loginRequest: LoginRequest): Promise<TokenResponse> {
    const response = await fetch(`${config.url}/auth`, {
        method: "POST",
        body: JSON.stringify(loginRequest)
    })

    if(!response.ok) {
        throw new Error("Failed to log in")
    }

    return response.json()
}

export async function getUser(userId: string): Promise<UserResponse> {
    const token = localStorage.getItem('_auth_token')
    const response = await fetch(`${config.url}/api/user/${userId}`, {
        method: "GET",
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    })

    return response.json()
}

export async function getCurrentUser(): Promise<UserResponse> {
    const token = localStorage.getItem("_auth_token")
    if(!token) {
        throw new Error("Not logged in")
    }

    const response = await fetch(`${config.url}/api/currentuser`, {
        method: "GET",
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    })

    return response.json()
}

export async function patchUser(req: PatchUserRequest, userId: string): Promise<UserResponse> {
    const token = localStorage.getItem("_auth_token")
    if(!token) {
        throw new Error("Not logged in")
    }

    const response = await fetch(`${config.url}/api/user/${userId}`, {
        method: "PATCH",
        body: JSON.stringify(req),
        headers: new Headers({"Authorization": `Bearer ${token}`, "Content-Type": "application/json"})
    })

    return response.json()
}

export default UserResponse
