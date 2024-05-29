import {enUS, pt} from 'date-fns/locale';


/**
 * Gets the current Locale object. For instance, it is useful to use on DateControl components.
 * @param lang Current language.
 */
const getCurrentLocale = (lang: string) => {
  return lang === 'pt' ? pt : enUS;
}


export {getCurrentLocale};
