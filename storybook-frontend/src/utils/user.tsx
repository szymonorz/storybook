import { config } from "./config"

interface UserResponse {
    id: string,
    username: string
}

interface CreateUserRequest {
    email: string,
    username: string,
    password: string
}

export async function registerUser(userRequest: CreateUserRequest) {
    console.log(userRequest)

    const response = await fetch(`${config.url}:${config.port}/api/user/register`, {
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

export async function loginUser(data: any) {
    const loginRequest = {
        email: data.email.value,
        password: data.password.value
    }

    console.log(loginRequest)

    const response = await fetch(`${config.url}/api/auth`, {
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

export default UserResponse
