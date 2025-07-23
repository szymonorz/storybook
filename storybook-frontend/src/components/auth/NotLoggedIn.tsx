import { useTranslation } from "react-i18next"
import LoginForm from "./LoginForm"

export default function NotLoggedIn() {
    const {t} = useTranslation()
    return (
        <div className="page">
            <div className="main-component">
                <h1>{t("must_be_logged_in")}</h1>
               <LoginForm/>
            </div>
        </div>
    )
}