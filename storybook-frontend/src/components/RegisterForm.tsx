import { useTranslation } from "react-i18next";
import { registerUser } from "../utils/user";

export default function RegisterForm() {
    const { t } = useTranslation()
    return (
        <form className="register-form" onSubmit={(event) => {
            event.preventDefault()
            registerUser(event.target)
        }}>
            <h1>Create an account</h1>
            <label className="form-field">
                <span className="form-text"> {t("register_form.username")} </span>
                <input className="form-input" id="username" name="username" type="text"/>
            </label>
            <label className="form-field">
                <span className="form-text"> {t("register_form.email")} </span>
                <input className="form-input" id="email" name="email" type="text"/>
            </label>
            <label className="form-field">
                <span className="form-text"> {t("register_form.password")} </span>
                <input className="form-input" id="password" name="password" type="password"/>
            </label>
            <label className="form-field">
                <span className="form-text"> {t("register_form.confirm_password")} </span>
                <input className="form-input" id="confirm-password" name="confirm-password" type="password"/>
            </label>
            <input type="submit" value="Register" />
        </form>
    )
}