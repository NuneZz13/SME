import {faPaperPlane} from '@fortawesome/free-solid-svg-icons';
import {FormattedValue, useFormContext, useFormManager} from '@kform/react';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogBody,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
  Button,
  Field,
  FieldGroup,
  FieldGroupBody,
  FieldGroupHeader,
  FieldGroupTitle,
  Label,
  Stack,
  combineEventHandlers
} from 'at-ui';
import {useFormAppContext} from 'at-ui-kform';
import {FormEvent, useState} from 'react';
import {useTranslation} from 'react-i18next';

export const SubmitBtn = () => {

  const {t} = useTranslation();

  const {submitting} = useFormContext();

  return (
    <Button
      type="submit"
      color="success"
      icon={faPaperPlane}
      iconPlacement="end"
      loading={submitting}
    >
      {t("common:actions.submit")}
    </Button>
  )
}



/* TODO [NG] the following needs to be fixed */

export interface SubmitActionProps {
  onCloseAutoFocus?: (event: Event) => void;
}

export function SubmitAction({onCloseAutoFocus}: SubmitActionProps) {

  const {t} = useTranslation();

  const formManager = useFormManager();
  const {startIssuesNavigation} = useFormAppContext();

  const [showConfirmation, setShowConfirmation] = useState<boolean>(false);
  const [submitting, setSubmitting] = useState(false);


  async function confirmSubmission(evt: FormEvent) {
    evt.preventDefault();
    setSubmitting(true);
    if (await formManager.isValid()) {
      setShowConfirmation(true);
    } else {
      try {
        await startIssuesNavigation();
      } finally {
        setSubmitting(false);
      }
    }
  }

  function handleOpenChange(open: boolean) {
    if (!open) {
      setSubmitting(false);
    }
  }

  return (
    <AlertDialog
      open={showConfirmation}
      onOpenChange={combineEventHandlers(handleOpenChange, setShowConfirmation)}
    >
      <AlertDialogTrigger>
        <Button
          type="submit"
          color="success"
          icon={faPaperPlane}
          iconPlacement="end"
          onClick={confirmSubmission}
          loading={submitting}
        >
          {t("common:actions.submit")}
        </Button>
      </AlertDialogTrigger>

      <AlertDialogContent color="success" onCloseAutoFocus={onCloseAutoFocus}>
        <AlertDialogHeader>
          <AlertDialogTitle>{t("common:confirmationDialog.title")}</AlertDialogTitle>
        </AlertDialogHeader>

        <AlertDialogBody>
          <AlertDialogDescription asChild>
            <Stack>

              <FieldGroup>
                <FieldGroupHeader code="0">
                  <FieldGroupTitle>{t("kform:marathon.personalInfo.page.title")}</FieldGroupTitle>
                </FieldGroupHeader>
                <FieldGroupBody>
                  <Stack>

                    <Field>
                      <Stack direction="column">
                        <Label>
                          {t('kform:marathon.personalInfo.name.label')}
                        </Label>
                        <FormattedValue path="/personalInfo/name"/>
                      </Stack>
                    </Field>

                  </Stack>
                </FieldGroupBody>
              </FieldGroup>


              <FieldGroup>
                <FieldGroupHeader code="1">
                  <FieldGroupTitle>{t("kform:marathon.fitness.page.title")}</FieldGroupTitle>
                </FieldGroupHeader>
                <FieldGroupBody>
                  <Stack>

                    <Field>
                      <Stack direction="column">
                        <Label>
                          {t('kform:marathon.fitness.rate.label')}
                        </Label>
                        <FormattedValue path="/fitnessLevel/rate"/>
                      </Stack>
                    </Field>

                  </Stack>
                </FieldGroupBody>
              </FieldGroup>

              <span>...</span>

            </Stack>
          </AlertDialogDescription>
        </AlertDialogBody>

        <AlertDialogFooter>
          <AlertDialogCancel>
            <Button>{t("common:confirmationDialog.cancel")}</Button>
          </AlertDialogCancel>

          <AlertDialogAction>
            <Button color="success" type="submit">
              {t("common:confirmationDialog.confirm")}
            </Button>
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
