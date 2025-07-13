import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router"

export default function UserNav() {
    const {t} = useTranslation()
    const navigate = useNavigate()
    return (
        <div>
            <ul>
                <li className="clickable" onClick={() => navigate("/books")}>{t("user-nav.my-books")}</li>
                <li className="clickable" onClick={() => navigate("/createBook")}>{t("user-nav.create-book")}</li>
                <li className="clickable" onClick={() => navigate("/favourites")}>{t("user-nav.favourites")}</li>
                <li className="clickable" onClick={() => navigate("/settings")}>{t("user-nav.settings")}</li>
            </ul>
        </div>
    )
}