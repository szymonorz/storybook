import { useNavigate } from "react-router"

export default function PageHeader() {
    const navigate = useNavigate()
    return <div className="page-header" onClick={() => navigate("/")}>
        StoryBook
    </div>
}