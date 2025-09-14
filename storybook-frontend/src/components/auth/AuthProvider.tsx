import { ReactNode, useLayoutEffect, useState } from "react";
import { createContext } from 'react'
import { getCurrentUser } from "../../utils/api/user";

export interface Auth {
    auth: string | null,
    privileges: string[],
    setAuth: (c: string | null) => void,
    setPrivileges: (p: string[]) => void 
}

export const AuthContext = createContext<Auth>({
    auth: null,
    privileges: [],
    setAuth: () => {},
    setPrivileges: () => {}
})

type AuthProviderProps = {
    children: ReactNode;
};

export default function AuthProvider({children}: AuthProviderProps) {
    const [auth, setAuth] = useState<string| null>(null)
    const [privileges, setPrivileges] = useState<string[]>([])

    useLayoutEffect(() => {
        const token = localStorage.getItem("_auth_token")
        if(token != null) {
            getCurrentUser()
                .then((userResp) => {
                    setAuth(token)
                    const _tmp: string[] = []
                    userResp.userRoles.forEach(role => _tmp.push(...role.privileges))
                    setPrivileges(_tmp)
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
        <AuthContext.Provider value={{auth, privileges, setAuth, setPrivileges}}>
            {children}
        </AuthContext.Provider>
    )
}