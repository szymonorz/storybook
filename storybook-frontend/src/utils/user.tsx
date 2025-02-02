interface UserResponse {
    id: string,
    username: string
}

interface CreateUserRequest {
    email: string,
    username: string,
    password: string
}

export function registerUser(data: any) {
    const userRequest: CreateUserRequest = {
        email: data.email.value,
        username: data.username.value,
        password: data.password.value
    }

    console.log(userRequest)
}

export default UserResponse
