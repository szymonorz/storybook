import { useNavigate } from "react-router"
import { useContext } from "react"
import { AuthContext } from "../auth/AuthProvider"
import SearchBar from "../search/SearchBar"

export default function PageHeader() {
    const navigate = useNavigate()
    const { auth, setAuth } = useContext(AuthContext)
    return <div className="page-header">
        <div onClick={() => navigate("/")}>
            StoryBook
        </div>
        <SearchBar/>
        {auth != null ? (
        <div className="logout" onClick={() => {
            localStorage.removeItem("_auth_token")
            navigate("/")
            setAuth(null)
        }}>
            Log out
        </div>) : null}
    </div>
}