import {faFolderOpen} from '@fortawesome/free-solid-svg-icons';
import {AbsolutePath} from '@kform/core';
import {useFormManager} from '@kform/react';
import {decodePriorNotificationForm} from 'SMEFrontEndKform-common';
import {Button, useToastManager} from 'at-ui';
import {ChangeEvent, useRef, useState} from 'react';
import {useTranslation} from 'react-i18next';


export interface LoadActionProps {
  onLoadingChange: (loading: boolean) => void;
}


export function LoadAction({ onLoadingChange }: LoadActionProps) {

  const { t } = useTranslation();

  const { addToast } = useToastManager();
  const formManager = useFormManager();

  const fileInputEl = useRef<HTMLInputElement | null>(null);
  const [loading, setLoading] = useState(false);


  function load() {
    if (fileInputEl.current) {
      fileInputEl.current.click();
      setLoading(true);
      onLoadingChange(true);
      window.addEventListener("focus", handleFocusBack);
    }
  }

  function handleFocusBack() {
    window.removeEventListener("focus", handleFocusBack);
    setLoading(false);
    onLoadingChange(false);
  }

  function handleFileChange(event: ChangeEvent<HTMLInputElement>) {
    async function onLoad(event: ProgressEvent<FileReader>) {
      try {
        if (event.target?.result) {
          await formManager.set(
            AbsolutePath.ROOT,
              decodePriorNotificationForm(event.target.result as string)
          );
          await formManager.setPristine(AbsolutePath.ROOT);
          await formManager.setTouched(AbsolutePath.MATCH_ALL);
        }
      } catch (e) {
        addToast(t("common:errors.loading.description"), {
          title: t("common:errors.loading.title"),
          severity: "danger",
        });
      } finally {
        setLoading(false);
        onLoadingChange(false);
      }
    }

    const reader = new FileReader();
    if (event.target.files) {
      reader.onload = onLoad;
      reader.readAsText(event.target.files[0]);
    }
    window.removeEventListener("focus", handleFocusBack);
  }


  return (
    <>
      <Button vertical onClick={load} icon={faFolderOpen} loading={loading}>
        {t("common:actions.load")}
      </Button>

      <input
        type="file"
        accept="application/json"
        ref={fileInputEl}
        onChange={handleFileChange}
        hidden
      />
    </>
  );
}
