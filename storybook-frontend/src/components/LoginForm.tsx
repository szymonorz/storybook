import { useNavigate } from "react-router"

export default function LoginForm() {
    const navigate = useNavigate()

    return (
        <form className="login-form">
            <label className="form-field">
                <span className="form-text">Email</span>
                <input className="form-input" id="email" name="email" type="text"/>
            </label>
            <label className="form-field">
                <span className="form-text">Password</span>
                <input className="form-input" id="password" name="password" type="password"/>
            </label>
            <input type="submit" value="Log in"/>
            <label>
                Don't have an account yet? <span className="clickable" onClick={() => navigate("/register")}>Register today</span>
            </label>
        </form>
    )
}