
export default function RegisterForm() {
    return (
        <form className="register-form">
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
            <input type="submit" value="Register"/>
        </form>
    )
}