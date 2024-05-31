import { enUS, ptPT } from 'at-ui-kform';
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

//Importar traduções
import common_en from './locales/en/common.json';
import errorPage_en from './locales/en/errorPage.json';
import kform_en from './locales/en/notificacaoPreviaForm.json';
import successPage_en from './locales/en/successPage.json';

import common_pt from './locales/pt/common.json';
import errorPage_pt from './locales/pt/errorPage.json';
import kform_pt from './locales/pt/notificacaoPreviaForm.json';
import successPage_pt from './locales/pt/successPage.json';






export const resources = {
  pt: {
    common: common_pt,
    errorPage: errorPage_pt,
    notificacaoPreviaForm: kform_pt,
    successPage: successPage_pt,
  },
  en: {
    common: common_en,
    errorPage: errorPage_en,
    notificacaoPreviaForm: kform_en,
    successPage: successPage_en,
  },
};

declare module "i18next" {
  interface CustomTypeOptions {
    resources: (typeof resources)["en"];
    returnNull: false;
  }
}

i18n.use(initReactI18next).init({
  lng: "pt",
  fallbackLng: "en",
  interpolation: {
    escapeValue: false,
  },
  resources,
  returnNull: false,
});


/**
 * Returns the AT-UI-KForm locale given the i18n language.
 * @param lng I18n language.
 */
export function atUiKFormLocale(lng: string) {
  return lng === "pt" ? ptPT : enUS;
}
