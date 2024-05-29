package pt.at.sme.kotlin.util

object ValidationRegexs {
    /**
    * Expressão regular de um URL. Adaptado de http://regexlib.com/REDetails.aspx?regexp_id=1854 . Melhoramentos:
    *
    *  * torna o "http://" ou "https://" opcional.
    *  * permite que URLs cujo path não acabe no nome dum ficheiro com extensão tenham parâmetros i.e. "example.com?x=true", "example.com/?x=true",
    * "example.com/pagina?x=true" funcionam. Na original só funcionaria "example.com/pagina.php?x=true" porque procurava o '.'.
    *  * Permite URLs válidos mas "estúpidos", por exemplo, URLs que acabem em '?' ou em '&' depois do '?'. Permite também que variáveis de GET não tenham um
    * valor definido, i.e. "[...]?x=&y=&z=blabla".
    *
    */
    val website:Regex = Regex("^(?:https?://)?" +
            // protocolo
            "[a-zA-Z0-9\\-_]+(?:\\.[a-zA-Z0-9\\-_]+)*\\.[a-zA-Z]{2,6}" +
            // domínio
            "(?:/?|(?:/[\\w\\-\\.]+/?)*)" +
            // path
            "(?:\\?|\\?[\\w]+(?:=[\\w\\-]*))?" +
            // 1º parâmetro
            "(?:&|&[\\w]+(?:=[\\w\\-]*))*$")


    val phoneNumber = Regex("(\\+)?[0-9 \\-]{0,20}")


}