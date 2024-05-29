import {FormManager, IterableKt, LocatedValidationIssueKt, SchemaKt} from '@kform/core';
import {
  ActivityType,
  LegalForm,
  MemberState,
  PriorNotificationForm,
  decodeLocatedValidationIssues,
  encodePriorNotificationForm,
  priorNotificationFormSchema
} from 'SMEFrontEndKform-common';
import {ControlAddon, Grid, Label, Separator, Stack, spacing, useToastManager} from 'at-ui';
import {
  CheckboxControl,
  ControlField,
  FormApp,
  FormAppApi,
  IssueMessage,
  IssuesNavigation,
  IssuesPopover,
  NumericControl,
  SelectControl,
  SelectMultipleControl,
  TableControl,
  TableControlAddRowTrigger,
  TableControlContent,
  TableControlRemoveRowTrigger,
  TextControl,
  TopBar, tableControlActionsColumn, tableControlIndexColumn
} from 'at-ui-kform';

import {useMemo, useRef, useState} from 'react';
import {Helmet} from 'react-helmet-async';
import {useTranslation} from 'react-i18next';
import {useLocation, useNavigate} from 'react-router';
import {LoadAction, SaveAction, SubmitBtn, ValidateAction} from '../components/forms/actions';
import {atUiKFormLocale} from '../i18n';
import './../globals.scss';




export function NotificacaoPreviaForm() {

  const {i18n, t} = useTranslation();


  const {pathname} = useLocation();
  const navigate = useNavigate();

  const { addToast } = useToastManager();
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const formAppApi = useRef<FormAppApi>();


  /**
   * Website table columuns
   */
  const columnWebsites = useMemo(
      () => [
        tableControlIndexColumn(),
        {
          label: t("Website(s)"),
          render: () => (
              <TextControl type="text" required
                           endAdornment={
                             <IssuesPopover>
                               <IssueMessage code="match">
                                 {t("notificacaoPreviaForm:contact.websites.issueMessages.match")}
                               </IssueMessage>
                               <IssueMessage code="notEmpty">
                                 {t("notificacaoPreviaForm:contact.websites.issueMessages.notEmpty")}
                               </IssueMessage>

                             </IssuesPopover>}
              />
          ),
        },
        tableControlActionsColumn({
          width: 80,
          render: () => (
              <Stack direction="row" gap={1} justifyContent="center">
                <TableControlRemoveRowTrigger defaultButtonLabel={"Remover"} />
              </Stack>
          ),
        }),
      ],
      [t]
  );



  /** Member State of Exemption table */
  const columnsMemberStatesOfExemption = useMemo(
      () => [
        tableControlIndexColumn(),
        {
          label: t("notificacaoPreviaForm:memberStatesOfExemption.memberState"),
          render: () => (
              <SelectControl
                  placeholder = {t(`common:select.chooseOne`)}
                  required
                  options={MemberState.values().map((v) => ({
                    value: v.ueCode,
                    text: v.ueCode + " - " + v.name,
                    key: v.name,
                  }))}

                  endAdornment={
                    <IssuesPopover>
                      <IssueMessage code="disallowedValue">
                        {t("notificacaoPreviaForm:common.totalSuppliesTable.issueMessages.disallowedValue")}
                      </IssueMessage>
                    </IssuesPopover>}
              />
          ),
        },
        tableControlActionsColumn({
          width: 80,
          render: () => (
              <Stack direction="row" gap={1} justifyContent="center">
                <TableControlRemoveRowTrigger defaultButtonLabel={"Remover"} />
              </Stack>
          ),
        }),
      ],
      [t]
  );


  const columnsTotalSupplies = useMemo(() => [
    tableControlIndexColumn(),
    {
      label: t("notificacaoPreviaForm:common.totalSuppliesTable.memberState"),
      render: () => (
          <SelectControl
              placeholder = {t(`common:select.chooseOne`)}
              path={"memberState"}
              required
              options={MemberState.values().map((v) => ({
                value: v.ueCode,
                text: v.ueCode + " - " + v.name,
                key: v.name,
              }))}

              endAdornment={
                <IssuesPopover path={"memberState"}>
                  <IssueMessage code="disallowedValue">
                    {t("notificacaoPreviaForm:common.totalSuppliesTable.issueMessages.disallowedValue")}
                  </IssueMessage>
                </IssuesPopover>}

          />
      ),
    },
    {
      label: t("notificacaoPreviaForm:common.totalSuppliesTable.total"),
      render: () => <NumericControl path={"totalValue"}
                                    groupDigits
                                    align="right"
                                    scale={2}
                                    endAdornment={
                                        <>
                                          <ControlAddon>€</ControlAddon>
                                          <IssueMessage code="min">
                                          {t("notificacaoPreviaForm:common.totalSuppliesTable.issueMessages.min")}
                                          </IssueMessage>
                                        </>}
                  />,
      width: 70,
    },
    tableControlActionsColumn({
      width: 80,
      render: () => (
          <Stack direction="row" gap={1} justifyContent="center">
            <TableControlRemoveRowTrigger defaultButtonLabel={"Remover"} />
          </Stack>
      ),
    }),
  ], [t]);

  /**
   * Handle the form submission
   * @param value - Form data
   * @param formManager - Form manager
   */
  const handleSubmission = async (value: unknown,  formManager: FormManager) => {

    try {

      const res = await fetch("/api/submit-prior-notification-form", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: encodePriorNotificationForm(value as PriorNotificationForm),
      });

      if (res.ok) {
        navigate("/success");
      } else if (res.status === 400) {
        const issuesJson: string = await res.text();
        const issuesKt = decodeLocatedValidationIssues(
            issuesJson
        ) as IterableKt<LocatedValidationIssueKt>;
        await formManager.addExternalIssues(issuesKt);
        await formAppApi.current?.startIssuesNavigation();
      } else {
        addToast(t("common:errors.submitting.description"), {
          title: t("common:errors.submitting.title"),
          severity: "danger",
        });
      }
    } catch (e) {
      addToast(t("common:errors.submitting.description"), {
        title: t("common:errors.submitting.title"),
        severity: "danger",
      });
    }

  };



  return (
    <>
      <FormApp
        schema={priorNotificationFormSchema.get() as SchemaKt}
        onSubmit={handleSubmission}
        apiRef={formAppApi}
        locale={atUiKFormLocale(i18n.language)}
        activePath={pathname}
        onActivePathChange={(activePath) =>
            navigate(activePath?.toString() ?? "/")
        }
        disabled={loading || saving}
        minHeight={600}
      >
        <Helmet>
          <title>{t("notificacaoPreviaForm:doc.title")}</title>
          <meta name="description" content={t("notificacaoPreviaForm:doc.descr")}/>
        </Helmet>

        <TopBar>
          <h1>{t("notificacaoPreviaForm:doc.title")}</h1>

          <Stack direction="row" gap={2} style={{marginBottom: spacing(1)}}>
            {/* Actions */}
            <LoadAction onLoadingChange={setLoading}/>
            <SaveAction onSavingChange={setSaving}/>
            <ValidateAction/>
            <SubmitBtn/>
          </Stack>
        </TopBar>

        {/* Identification */}
        <Stack gap={0}>
          <h2>{t("notificacaoPreviaForm:identification.title")}</h2>
          <Grid container>
            {/* vatIdentificationNumber */}
            <Grid xs={12} md={2}>
              <ControlField>
                <Label>{t("notificacaoPreviaForm:identification.vatIdentificationNumber.label")}</Label>
                <TextControl startAdornment={<ControlAddon>PT</ControlAddon>} type="text" required path={"/identification/vatIdentificationNumber/vatIdNum"}/>
                <IssueMessage path={"/identification/vatIdentificationNumber/vatIdNum"} code="notEmpty">
                  {t("notificacaoPreviaForm:identification.vatIdentificationNumber.issueMessages.notEmpty")}
                </IssueMessage>
                <IssueMessage path={"/identification/vatIdentificationNumber/issuingCountry"} code="notEmpty">
                  {t("notificacaoPreviaForm:identification.vatIdentificationNumber.issueMessages.notEmpty")}
                </IssueMessage>
                <IssueMessage path={"/identification/vatIdentificationNumber/issuingCountry"} code="disallowedValue">
                  {t("notificacaoPreviaForm:identification.vatIdentificationNumber.issueMessages.disallowedValue")}
                </IssueMessage>
              </ControlField>
            </Grid>
          </Grid>

          <Grid container>
              {/* Taxable person name */}
              <Grid xs={12} md={4}>
                  <ControlField path={"/identification/name"}>
                    <Label>{t("notificacaoPreviaForm:identification.name.label")}</Label>
                    <TextControl type="text" required/>
                    <IssueMessage code="notEmpty">
                      {t("notificacaoPreviaForm:identification.name.issueMessages.notEmpty")}
                    </IssueMessage>
                  </ControlField>
              </Grid>
          </Grid>

          <Grid container>
            {/* Activity Type */}
            <Grid xs={12} md={6}>
              <ControlField path={"/identification/activityType"}>
                <Label>{t("notificacaoPreviaForm:identification.activityType.label")}</Label>
                <SelectMultipleControl required
                                       options={ActivityType.values().map((v) => ({
                                         value: v.wdesc,
                                         text: v.name,
                                         key: v.name,
                                       }))} />
                <IssueMessage code="minSize">
                  {t("notificacaoPreviaForm:identification.activityType.issueMessages.minSize")}
                </IssueMessage>
              </ControlField>
            </Grid>

            {/* Legal Form */}
            <Grid xs={12} md={6}>
              <ControlField path={"/identification/legalForm"}>
                <Label>{t("notificacaoPreviaForm:identification.legalForm.label")}</Label>
                <SelectControl required
                               placeholder = {t(`common:select.chooseOne`)}
                               options={LegalForm.values().map((v) => ({
                                 value: v.value,
                                 text: t(`common:legalForm.options.${v.name}`),
                                 key: v.name,
                               }))} />
                <IssueMessage code="notEmpty">
                  {t("notificacaoPreviaForm:identification.legalForm.issueMessages.notEmpty")}
                </IssueMessage>
                <IssueMessage code="disallowedValue">
                  {t("notificacaoPreviaForm:identification.legalForm.issueMessages.disallowedValue")}
                </IssueMessage>
              </ControlField>
            </Grid>
          </Grid>

          <Grid container>
            {/* Is registered in OSS union */}
            <Grid xs={12} md={6}>
              <Stack>
                <ControlField path={"/identification/hasOSSRegistration"}>
                  <CheckboxControl children={t("notificacaoPreviaForm:identification.hasOSSRegistration.label")}/>
                </ControlField>
                <IssueMessage code={"ossNumberFilledAndCheckBoxHasRegisteredInOSSNotChecked"}>
                  {t("notificacaoPreviaForm:identification.hasOSSRegistration.issueMessages.ossNumberFilledAndCheckBoxHasRegisteredInOSSNotChecked")}
                </IssueMessage>
              </Stack>

              {/* OSS union */}
              <ControlField path={"/identification/ossNumber"}>
                <Label>{t("notificacaoPreviaForm:identification.ossNumber.label")}</Label>
                <TextControl type="text" />
              </ControlField>
              <IssueMessage code={"ossNumberMandatoryIfHasRegisteredInOSS"}>
                {t("notificacaoPreviaForm:identification.ossNumber.issueMessages.ossNumberMandatoryIfHasRegisteredInOSS")}
              </IssueMessage>

            </Grid>
          </Grid>
        </Stack>

        {/* End Identification */}

        <Separator style={{marginTop: spacing(5)}} />

        {/* Contacts */}
        <Stack gap={0}>
          <h2>{t("notificacaoPreviaForm:contact.title")}</h2>
          <Grid container>
            {/* Street */}
            <Grid xs={12} md={7}>
              <ControlField path={"/contact/street"}>
                <Label>{t("notificacaoPreviaForm:contact.street.label")}</Label>
                <TextControl required />
                <IssueMessage code="notEmpty">
                  {t("notificacaoPreviaForm:contact.street.issueMessages.notEmpty")}
                </IssueMessage>
              </ControlField>
            </Grid>

            {/* Number */}
            <Grid xs={12} md={2}>
              <ControlField path={"/contact/number"}>
                <Label>{t("notificacaoPreviaForm:contact.number.label")}</Label>
                <TextControl required />
                <IssueMessage code="notEmpty">
                  {t("notificacaoPreviaForm:contact.number.issueMessages.notEmpty")}
                </IssueMessage>
              </ControlField>
            </Grid>

            {/* Post Code */}
            <Grid xs={12} md={2}>
              <ControlField path={"/contact/postCode"}>
                <Label>{t("notificacaoPreviaForm:contact.postCode.label")}</Label>
                <TextControl required />
                <IssueMessage code="notEmpty">
                  {t("notificacaoPreviaForm:contact.postCode.issueMessages.notEmpty")}
                </IssueMessage>
              </ControlField>
            </Grid>

            {/* City */}
            <Grid xs={12} md={3}>
              <ControlField path={"/contact/city"}>
                <Label>{t("notificacaoPreviaForm:contact.city.label")}</Label>
                <TextControl required />
                <IssueMessage code="notEmpty">
                  {t("notificacaoPreviaForm:contact.city.issueMessages.notEmpty")}
                </IssueMessage>
              </ControlField>
            </Grid>

            {/* Country */}
            <Grid xs={12} md={4}>
              <ControlField path={"/contact/country"}>
                <Label>{t("notificacaoPreviaForm:contact.country.label")}</Label>
                <SelectControl required
                               placeholder = {t(`common:select.chooseOne`)}
                               options={MemberState.values().map((v) => ({
                                 value: v.ueCode,
                                 text: v.name,
                                 key: v.name,
                               }))}
                />
                <IssueMessage code="notEmpty">
                  {t("notificacaoPreviaForm:contact.country.issueMessages.notEmpty")}
                </IssueMessage>
                <IssueMessage code="disallowedValue">
                  {t("notificacaoPreviaForm:contact.country.issueMessages.disallowedValue")}
                </IssueMessage>
              </ControlField>
            </Grid>
          </Grid>

          <Grid container>
          {/* Phone Number*/}
            <Grid xs={12} md={6}>
              <ControlField path={"/contact/phoneNumber"}>
                <Label>{t("notificacaoPreviaForm:contact.phoneNumber.label")}</Label>
                <TextControl />
                <IssueMessage code="match">
                  {t("notificacaoPreviaForm:contact.phoneNumber.issueMessages.match")}
                </IssueMessage>
              </ControlField>
            </Grid>


            {/* Email Address */}
            <Grid xs={12} md={6}>
              <ControlField path={"/contact/emailAddress"}>
                <Label>{t("notificacaoPreviaForm:contact.emailAddress.label")}</Label>
                <TextControl />
                <IssueMessage code={"invalidEmail"}>
                  {t("notificacaoPreviaForm:contact.emailAddress.issueMessages.invalidEmail")}
                </IssueMessage>
              </ControlField>
            </Grid>
          </Grid>

          <Grid container>
          {/*Website(s)*/}
            <Grid xs={12} md={12}>
              <ControlField path={"/contact/websites"}>
                <TableControl columns={columnWebsites}>
                  <TableControlAddRowTrigger
                      defaultButtonText={t("notificacaoPreviaForm:contact.websites.addValue")}
                  />
                  <TableControlContent emptyMessage={t("common:table.noResults")} />
                </TableControl>
              </ControlField>
            </Grid>
          </Grid>
          </Stack>
        {/* End Contacts */}

        <Separator style={{marginTop: spacing(5)}} />


        {/* Member State(s) in which you intend to avail yourself of exemption */}
        <Stack gap={0}>
          <h2>{t("notificacaoPreviaForm:memberStatesOfExemption.title")}</h2>
          <Grid container>
            <Grid xs={12} md={12}>
              <ControlField path={"/memberStatesOfExemption"}>
                <TableControl columns={columnsMemberStatesOfExemption}>
                  <TableControlAddRowTrigger
                        defaultButtonText={t("notificacaoPreviaForm:memberStatesOfExemption.addValue")}
                  />
                  <TableControlContent emptyMessage={t("common:table.noResults")} />
                </TableControl>
                <IssueMessage code="minSize">
                  {t("notificacaoPreviaForm:memberStatesOfExemption.issueMessages.minSize")}
                </IssueMessage>
                <IssueMessage code="repeatedElements">
                  {t("notificacaoPreviaForm:memberStatesOfExemption.issueMessages.repeatedElements")}
                </IssueMessage>
              </ControlField>
            </Grid>
          </Grid>
        </Stack>
        {/* End Member State(s) in which you intend to avail yourself of exemption */}

        <Separator style={{marginTop: spacing(5)}} />

        {/* TotalValueOfSuppliesByTaxablePerson */}
        <Stack gap={0}>
          <h2>{t("notificacaoPreviaForm:TotalValueOfSuppliesByTaxablePerson.title")}</h2>
          <Grid container>

            {/* currCalendar */}
            <Grid xs={12} md={12}>
              <h3>{t("notificacaoPreviaForm:TotalValueOfSuppliesByTaxablePerson.currCalendar.label")}</h3>
              <ControlField path={"/totalSuppliesCurrCalendar"}>
                  <TableControl columns={columnsTotalSupplies}>
                    <TableControlAddRowTrigger
                        defaultButtonText={t("notificacaoPreviaForm:common:totalSuppliesTable.addValue")}
                    />
                    <TableControlContent emptyMessage={t("common:table.noResults")} />
                  </TableControl>

                <IssueMessage code="minSize">
                  {t("notificacaoPreviaForm:TotalValueOfSuppliesByTaxablePerson.currCalendar.issueMessages.minSize")}
                </IssueMessage>
                <IssueMessage code="repeatedElements">
                  {t("notificacaoPreviaForm:common.totalSuppliesTable.issueMessages.repeatedElements")}
                </IssueMessage>
                <IssueMessage code="portugalNotExistsOnTable">
                  {t("notificacaoPreviaForm:common.totalSuppliesTable.issueMessages.portugalNotExistsOnTable")}
                </IssueMessage>

                </ControlField>
              </Grid>

            {/* prevCalendar */}
            <Grid xs={12} md={12}>
              <h3>{t("notificacaoPreviaForm:TotalValueOfSuppliesByTaxablePerson.prevCalendar.label")}</h3>
              <ControlField path={"/totalSuppliesPrevCalendar"}>
                <TableControl columns={columnsTotalSupplies}>
                  <TableControlAddRowTrigger
                      defaultButtonText={t("notificacaoPreviaForm:common.totalSuppliesTable.addValue")}
                  />
                  <TableControlContent emptyMessage={t("common:table.noResults")} />
                </TableControl>
                <IssueMessage code="repeatedElements">
                  {t("notificacaoPreviaForm:common.totalSuppliesTable.issueMessages.repeatedElements")}
                </IssueMessage>
                <IssueMessage code="portugalNotExistsOnTable">
                  {t("notificacaoPreviaForm:common.totalSuppliesTable.issueMessages.portugalNotExistsOnTable")}
                </IssueMessage>
              </ControlField>
            </Grid>

            {/* priorYearToPrevCalendar */}
           <Grid xs={12} md={12}>
             <h3>{t("notificacaoPreviaForm:TotalValueOfSuppliesByTaxablePerson.priorYearToPrevCalendar.label")}</h3>
             <ControlField path={"/totalSuppliesPriorYearToPrevCalendar"}>
                  <TableControl columns={columnsTotalSupplies}>
                    <TableControlAddRowTrigger
                        defaultButtonText={t("notificacaoPreviaForm:common:totalSuppliesTable.addValue")}
                    />
                    <TableControlContent emptyMessage={t("common:table.noResults")} />
                  </TableControl>
                 <IssueMessage code="repeatedElements">
                   {t("notificacaoPreviaForm:common.totalSuppliesTable.issueMessages.repeatedElements")}
                 </IssueMessage>
                 <IssueMessage code="portugalNotExistsOnTable">
                   {t("notificacaoPreviaForm:common.totalSuppliesTable.issueMessages.portugalNotExistsOnTable")}
                 </IssueMessage>
                </ControlField>
              </Grid>

              {/* identifiedInOtherMS */}
              <Grid xs={12} md={12}>
                <ControlField path={"/identifiedInOtherMS"}>
                  <CheckboxControl children={t("notificacaoPreviaForm:identifiedInOtherMS.label")}/>
                </ControlField>
              </Grid>
          </Grid>
        </Stack>
        {/* End TotalValueOfSuppliesByTaxablePerson */}

        <IssuesNavigation/>

      </FormApp>

    </>
  );
}
