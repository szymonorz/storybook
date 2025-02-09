import { useTranslation } from "react-i18next";
import { CreateUserRequest, registerUser } from "../utils/user";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";


interface RegisterFormValues {
    username: string,
    email: string,
    password: string,
    confirm_password: string

}

export default function RegisterForm() {
    const { t } = useTranslation()
    const [success, setSuccess] = useState<Boolean>(false)
    const [error, setError] = useState<number>(-1)

    const {register, formState, watch, handleSubmit} = useForm<RegisterFormValues>()
    const {errors} = formState

    useEffect(() => {
        console.log("hello?", success, error)
        if(!success && error != -1) {
            console.log("Failed to create user")
        }
    }, [success, error])

    function onSubmit(data: RegisterFormValues) {
            registerUser({
                username: data.username,
                email: data.email,
                password: data.password
            })
                .then((data) => {
                    
                    console.log(data)
                    if(data.status_code != 200) {
                        setSuccess(false)
                        setError(409)
                    } else {
                        setSuccess(true)
                        setError(data.status_code)
                    }
                })
                .catch(err => {
                    console.log("fail")
                    console.log(err)
                    setError(1)
                    setSuccess(false)
                })
    }

    return (
        <form className="register-form" onSubmit={handleSubmit(onSubmit)}>
            {
                (!success && error != -1) ? (
                    <div className="register-error">
                        <h1>{t("register_form.failed")}</h1>
                        <h3>{error == 409 ? t("register_form.in_use") : t("register_form.invalid_request")}</h3>
                    </div>   
            ) : null
                  
            }
            <h1>{t("register_form.create_account")}</h1>
            <label className="form-field">
                <span className="form-text"> {t("register_form.username")} </span>
                <input className="form-input" id="username" type="text" {...register("username", {
                    required: {
                        value: true,
                        message: t("register_form.field_required")
                    }
                })}/>
            </label>
            <span>{errors.username?.message}</span>
            <label className="form-field">
                <span className="form-text"> {t("register_form.email")} </span>
                <input className="form-input" id="email" type="email" {...register("email", {
                    required: {
                        value: true,
                        message: t("register_form.field_required")
                    }
                })}/>
            </label>
            <span>{errors.email?.message}</span>
            <label className="form-field">
                <span className="form-text"> {t("register_form.password")} </span>
                <input className="form-input" id="password" type="password" {...register("password",{
                        required: {
                            value: true,
                            message: t("register_form.field_required")
                        },
                        pattern: {
                            value: 
                                /^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,}$/,
                            message: t("register_form.password_criteria_not_met")
                        }
                })}/>
            </label>
            <div>{t("register_form.password_criteria")}</div>
            <ul>
                <li>{t("register_form.password_length")}</li>
                <li>{t("register_form.capital_letter")}</li>
                <li>{t("register_form.small_letter")}</li>
                <li>{t("register_form.number")}</li>
                <li>{t("register_form.symbol")}</li>
            </ul>
            <span>{errors.password?.message}</span>
            <label className="form-field">
                <span className="form-text"> {t("register_form.confirm_password")} </span>
                <input className="form-input" type="password" {...register("confirm_password", {
                    required: {
                        value: true,
                        message: t("register_form.field_required")
                    },
                    validate: (val: string) => {
                        if(watch("password") != val) {
                            return t("register_form.password_must_match")
                        }
                    }
                })}/>
                <span>{errors.confirm_password?.message}</span>
            </label>
            <input type="submit" value="Register" />
        </form>
    )
}