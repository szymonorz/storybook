import { useContext } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router"
import { AuthContext } from "../auth/AuthProvider"
import { RolePrivilege } from "../../utils/api/role"

export default function UserNav() {
    const {t} = useTranslation()
    const navigate = useNavigate()

    const {privileges} = useContext(AuthContext)
    return (
        <div>
            <ul>
                <li className="clickable" onClick={() => navigate("/books")}>{t("user-nav.my-books")}</li>
                <li className="clickable" onClick={() => navigate("/createBook")}>{t("user-nav.create-book")}</li>
                <li className="clickable" onClick={() => navigate("/settings")}>{t("user-nav.settings")}</li>

                {
                    privileges.some(priv => priv.startsWith("MODERATE_") || priv == "SUPERUSER") 
                    
                    ? 
                        <li className="clickable">{t("user-nav.moderate")}</li> 
                        : null
                    
                }
                {
                    privileges.some(priv => priv.endsWith("_ROLE") || priv == "SUPERUSER") 
                    
                    ? 
                        <li className="clickable">{t("user-nav.manage-roles")}</li> 
                        : null
                    
                }

            </ul>
        </div>
    )
}