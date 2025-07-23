import { useForm } from "react-hook-form"
import { useNavigate } from "react-router"
import { loginUser } from "../../utils/api/user"
import { useTranslation } from "react-i18next"
import { useContext, useState } from "react"
import { AuthContext } from "./AuthProvider"

interface LoginFormValues {
    username: string
    password: string
}

export default function LoginForm() {
    const navigate = useNavigate()
    const {t} = useTranslation()
    const {register, handleSubmit} = useForm<LoginFormValues>()
    const [error, setError] = useState<Boolean>(false)
    const { setAuth } = useContext(AuthContext)

    function onSubmit(data: LoginFormValues) {
        loginUser({
            username: data.username,
            password: data.password
        })
        .then((data) => {
                localStorage.setItem("_auth_token", data.access_token)
                setError(false)
                const token = localStorage.getItem("_auth_token")
                setAuth(token)
                navigate("/home")
        })
        .catch((err) => console.log(err))
    }

    return (
        <form className="login-form" onSubmit={handleSubmit(onSubmit)}>
            <label className="form-field">
                <span className="form-text">{t("login_form.username")}</span>
                <input className="form-input" id="username" type="text" {...register("username")}/>
            </label>
            <label className="form-field">
                <span className="form-text">{t("login_form.password")}</span>
                <input className="form-input" id="password" type="password" {...register("password")}/>
            </label>
            <input type="submit" value={t("login_form.login")}/>
            <label>
                {t("login_form.no_account")} <span className="clickable underline" onClick={() => navigate("/register")}>{t("login_form.register_today")}</span>
            </label>
            {error ? (<span className="login-error">{t("login_form.failed")}</span>): null }
        </form>
    )
}