import {Button, Card, CardBody, CardFooter, CardHeader, CardTitle, Grid, Stack} from "at-ui";
import { Helmet } from "react-helmet-async";
import { useTranslation } from "react-i18next";
import {useNavigate} from "react-router";

export function SuccessPage() {
    const { t } = useTranslation(["successPage"]);
    const navigate = useNavigate();


    return (
    <>
        <Helmet>
            <title>{t("title")}</title>
        </Helmet>

        <Stack>
            <Grid container>
                <Grid xs={6} md={6} style={{margin: "auto"}}>
                    <Card>
                        <CardHeader>
                            <CardTitle>
                                {t("heading")}
                            </CardTitle>
                        </CardHeader>
                        <CardBody>
                            {t("info")}
                        </CardBody>
                        <CardFooter>
                            <Button type={"submit"} onClick={() => navigate("/")}> {t("button")}</Button>
                        </CardFooter>
                    </Card>
                </Grid>
            </Grid>
        </Stack>
    </>
  );
}
