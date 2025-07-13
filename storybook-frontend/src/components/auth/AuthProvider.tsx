import { useLayoutEffect, useState } from "react";
import { createContext } from 'react'
import { getCurrentUser } from "../../utils/api/user";

export const AuthContext = createContext({})
export default function AuthProvider({children}) {
    const [auth, setAuth] = useState<string| null>(null)

    useLayoutEffect(() => {
        const token = localStorage.getItem("_auth_token")
        console.log(token)
        if(token != null) {
            getCurrentUser(token)
                .then((userData) => {
                    console.log(userData)
                    setAuth(token)
                }).catch((err) => {
                    console.log(err)
                    setAuth(null)
                })
        } else {
            setAuth(null)
        }

    }, [])

    return (
        <AuthContext.Provider value={{auth, setAuth}}>
            {children}
        </AuthContext.Provider>
    )
}