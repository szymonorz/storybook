import { useTranslation } from "react-i18next"
import LoginForm from "./LoginForm"
import { useSearchParams } from "react-router"
import { useEffect, useState } from "react"

export default function LoginPage() {
    const {t} = useTranslation()
    const [searchParams] = useSearchParams()

    const [redirectMessage, setRedirectMessage] = useState<string>()

    useEffect(() => {
        const redirect = searchParams.get("redirect")
        if(redirect) {
            setRedirectMessage(t(redirect))
        } else {
            setRedirectMessage(t("login.default"))
        }

    }, [searchParams])

    return (
        <div className="page">
            <div className="main-component">
                <h1>{redirectMessage}</h1>
               <LoginForm/>
            </div>
        </div>
    )
}