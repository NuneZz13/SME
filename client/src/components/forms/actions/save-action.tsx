import {faFloppyDisk} from '@fortawesome/free-solid-svg-icons';
import {AbsolutePath} from '@kform/core';
import {useFormManager} from '@kform/react';
import {PriorNotificationForm, encodePriorNotificationForm} from 'SMEFrontEndKform-common';
import {Button} from 'at-ui';
import {useState} from 'react';
import {useTranslation} from 'react-i18next';


export interface SaveActionProps {
  onSavingChange: (saving: boolean) => void;
}

export function SaveAction({ onSavingChange }: SaveActionProps) {

  const { t } = useTranslation();

  const formManager = useFormManager();

  const [saving, setSaving] = useState(false);


  async function save() {

    setSaving(true);
    onSavingChange(true);

    try {

      const json = await formManager.get<PriorNotificationForm, string>(
        new AbsolutePath("/"),
        (value) => encodePriorNotificationForm(value, true)
      );

      const jsonUri = `data:application/json;charset=utf-8,${encodeURIComponent(
        json
      )}`;

      const anchor = document.createElement("a");
      anchor.href = jsonUri;
      anchor.download = `SME-${new Date().toLocaleDateString()}.json`;
      anchor.click();

    } finally {
      setSaving(false);
      onSavingChange(false);
    }

  }


  return (
    <Button vertical onClick={save} icon={faFloppyDisk} loading={saving}>
      {t("common:actions.save")}
    </Button>
  );
}
