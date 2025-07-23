import { useForm } from "react-hook-form"
import { useNavigate } from "react-router"
import { loginUser } from "../../utils/api/user"
import { useTranslation } from "react-i18next"
import { useContext, useState } from "react"
import { AuthContext } from "./AuthProvider"

interface LoginFormValues {
    email: string
    password: string
}

export default function LoginForm() {
    const navigate = useNavigate()
    const {t} = useTranslation()
    const {register, handleSubmit} = useForm<LoginFormValues>()
    const [error, setError] = useState<Boolean>(false)
    const { auth, setAuth } = useContext(AuthContext)

    function onSubmit(data: LoginFormValues) {
        loginUser({
            email: data.email,
            password: data.password
        })
        .then((data) => {
            console.log(data)
            if(data instanceof Error) {
                setError(true)
            } else {
                setError(false)
                const token = localStorage.getItem("_auth_token")
                setAuth(token)
                navigate("/home")
            }
        })
        .catch((err) => console.log(err))
    }

    return (
        <form className="login-form" onSubmit={handleSubmit(onSubmit)}>
            <label className="form-field">
                <span className="form-text">{t("login_form.email")}</span>
                <input className="form-input" id="email" type="text" {...register("email")}/>
            </label>
            <label className="form-field">
                <span className="form-text">{t("login_form.password")}</span>
                <input className="form-input" id="password" type="password" {...register("password")}/>
            </label>
            <input type="submit" value="Log in"/>
            <label>
                {t("login_form.no_account")} <span className="clickable" onClick={() => navigate("/register")}>{t("login_form.register_today")}</span>
            </label>
            {error ? (<span className="login-error">{t("login_form.failed")}</span>): null }
        </form>
    )
}