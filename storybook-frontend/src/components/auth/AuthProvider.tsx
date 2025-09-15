import { ReactNode, useLayoutEffect, useState } from "react";
import { createContext } from 'react'
import { getCurrentUser } from "../../utils/api/user";

export interface Auth {
    auth: string | null,
    setAuth: (c: string | null) => void
}

export const AuthContext = createContext<Auth>({
    auth: null,
    setAuth: () => {}
})

type AuthProviderProps = {
    children: ReactNode;
};

export default function AuthProvider({children}: AuthProviderProps) {
    const [auth, setAuth] = useState<string| null>(null)

    useLayoutEffect(() => {
        const token = localStorage.getItem("_auth_token")
        if(token != null) {
            getCurrentUser()
                .then(() => {
                    setAuth(token)
                }).catch(() => {
                    console.log("hon2")
                    setAuth(null)
                    localStorage.removeItem("_auth_token")
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