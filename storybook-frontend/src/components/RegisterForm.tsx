import { registerUser } from "../utils/user";

export default function RegisterForm() {
    return (
        <form className="register-form" onSubmit={(event) => {
            event.preventDefault()
            registerUser(event.target)
        }}>
            <h1>Create an account</h1>
            <label className="form-field">
                <span className="form-text">Username</span>
                <input className="form-input" id="username" name="username" type="text"/>
            </label>
            <label className="form-field">
                <span className="form-text">Email</span>
                <input className="form-input" id="email" name="email" type="text"/>
            </label>
            <label className="form-field">
                <span className="form-text">Password</span>
                <input className="form-input" id="password" name="password" type="password"/>
            </label>
            <label className="form-field">
                <span className="form-text">Confirm password</span>
                <input className="form-input" id="confirm-password" name="confirm-password" type="password"/>
            </label>
            <input type="submit" value="Register" />
        </form>
    )
}