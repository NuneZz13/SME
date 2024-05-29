import {faCircleCheck} from '@fortawesome/free-regular-svg-icons';
import {Button} from 'at-ui';
import {useFormAppContext} from 'at-ui-kform';
import {useState} from 'react';
import {useTranslation} from 'react-i18next';

export function ValidateAction() {

  const { t } = useTranslation();

  const { startIssuesNavigation } = useFormAppContext();

  const [validating, setValidating] = useState(false);


  async function validate() {
    setValidating(true);
    try {
      await startIssuesNavigation();
    } finally {
      setValidating(false);
    }
  }

  return (
      <Button
          color="primary"
          icon={faCircleCheck}
          vertical
          onClick={validate}
          loading={validating}
      >
        {t("common:actions.validate")}
      </Button>
  );
}
