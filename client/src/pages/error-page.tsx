import { Heading } from "at-ui";
import { Helmet } from "react-helmet-async";
import { useTranslation } from "react-i18next";


export function ErrorPage() {

    const { t } = useTranslation(["errorPage"]);

    return (
        <>
            <Helmet>
                <title>{t("title")}</title>
            </Helmet>

            <div className="error-page">
                <Heading variant="display" level={2}>
                    {t("heading")}
                </Heading>
            </div>
        </>
    );
}
