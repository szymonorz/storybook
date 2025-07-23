import { ReactNode } from "react"

interface NavSegmentProps {
    children: ReactNode
}

export default function NavSegment({children}: NavSegmentProps) {
    return (
        <div className="nav-segment">
            {children}
        </div>
    )
}