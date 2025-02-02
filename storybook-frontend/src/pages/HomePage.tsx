import LoginForm from "../components/LoginForm";
import TopBooksSegment from "../components/TopBooksSegment";

export default function HomePage() {
    return (
        <div className="page">
            <TopBooksSegment/>
            <LoginForm/>
        </div>
    )
}