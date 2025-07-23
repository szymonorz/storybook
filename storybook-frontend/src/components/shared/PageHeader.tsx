import { useNavigate } from "react-router"
import { useContext } from "react"
import { AuthContext } from "../auth/AuthProvider"
import SearchBar from "../search/SearchBar"
import { useTranslation } from "react-i18next"
import LocalesSelector from "./LocalesSelector"

export default function PageHeader() {
    const navigate = useNavigate()
    const { auth, setAuth } = useContext(AuthContext)
    const {t} = useTranslation()
    return <div className="page-header">
        <div className="page-logo" onClick={() => navigate("/")}>
            StoryBook
        </div>
        <SearchBar/>
        <LocalesSelector/>
        {auth != null ? (
        <div className="logout" onClick={() => {
            localStorage.removeItem("_auth_token")
            navigate("/")
            setAuth(null)
        }}>
            {t("logout")}
        </div>) : null}
    </div>
}