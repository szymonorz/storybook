import i18n from "../../utils/i18n"

export default function LocalesSelector() {
    function changeLocale(lang: string) {
        i18n.changeLanguage(lang)
    }
    
    return <div className="locales-selector">
        <div className="clickable" onClick={() => changeLocale("en")}>ENG</div>
        <div className="clickable" onClick={() => changeLocale("pl")}>PL</div>
    </div>
}