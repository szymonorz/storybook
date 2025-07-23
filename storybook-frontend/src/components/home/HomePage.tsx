import { useContext } from "react";
import LoginForm from "../../components/auth/LoginForm";
import TopBooksSegment from "../../components/books/TopBooksSegment";
import { AuthContext } from "../../components/auth/AuthProvider";
import NavSegment from "../../components/shared/NavSegment";
import UserNav from "../../components/shared/UserNav";

export default function HomePage() {
    const {auth} = useContext(AuthContext)
    return (
        <div className="page">
            <TopBooksSegment/>
            <NavSegment>
                {auth == null ? <LoginForm/> : <UserNav/> }
            </NavSegment>
        </div>
    )
}