import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router"
import { getCurrentUser, patchUser } from "../../utils/api/user"

interface SettingsFormValues {
    email: string,
    password: string
}

export default function SettingsPage() {

    const {t} = useTranslation()
    const navigate = useNavigate()

    const [error, setError] = useState<string | null>()
    const [success, setSuccess] = useState<boolean>(false)
    const [userId, setUserId] = useState<string>()
    const [values, setValues] = useState<SettingsFormValues>()
    const {formState, register, handleSubmit} = useForm<SettingsFormValues>({
        defaultValues: {
            email: "",
            password: ""
        },
        values
    })

    const {errors} = formState

    useEffect(() => {
        console.log(success)
        getCurrentUser()
            .then((userResp) => {
                setValues({
                    email: userResp.email,
                    password: ""
                })
                setUserId(userResp.id)
            })
            .catch((err) => {
                setError(err)
                console.log("hon")
                navigate("/login?redirect=must_be_logged_in")
            })
    }, [])

    function onSubmit(data: SettingsFormValues) {
        patchUser(
            {
                email: data.email || null,
                password: data.password || null
            },
            userId! // should never be undefined
        ).then((userResp) => {
            setValues({
                email: userResp.email,
                password: ""
            })
            setSuccess(true)
        }).catch((err) => setError(err))
    }

    return <div className="page">
        <div className="main-component">
            { success ? <div>{t("settings_form.success")}</div> : null}
            { error ? <div>{t("settings_form.error")}</div> : null }
        <form className="settings-form" onSubmit={handleSubmit(onSubmit)}>
            <label className="form-field">
                <span className="form-text"> {t("settings_form.email")} </span>
                <input className="form-input" id="email" type="email" {...register("email", {
                    required: {
                        value: false,
                        message: t("register_form.field_required")
                    }
                })}/>
                <span className="form-validation-error">{errors.email?.message}</span>
            </label>
            <label className="form-field">
                <span className="form-text"> {t("settings_form.password")} </span>
                <input className="form-input" id="password" type="password" {...register("password",{
                        required: {
                            value: false,
                            message: t("register_form.field_required")
                        },
                        pattern: {
                            value: 
                                /^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,}$/,
                            message: t("register_form.password_criteria_not_met")
                        }
                })}/>
                <span className="form-validation-error">{errors.password?.message}</span>
                <div>{t("register_form.password_criteria")}</div>
                <ul>
                    <li>{t("register_form.password_length")}</li>
                    <li>{t("register_form.capital_letter")}</li>
                    <li>{t("register_form.small_letter")}</li>
                    <li>{t("register_form.number")}</li>
                    <li>{t("register_form.symbol")}</li>
                </ul>
            </label>
            <input type="submit" value={t("settings_form.save")}/>

            {error ? (<span className="login-error">{t("login_form.failed")}</span>): null }
        </form>
        </div>
    </div>
}