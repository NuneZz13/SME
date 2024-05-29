package pt.at.sme.kotlin.enums

import kotlin.js.JsExport

@JsExport
enum class LegalForm(val code: String, val value: String) {
    NATURAL_PERSON("Code-1","Natural Person"),
    PRIVATE_COMPANY("Code-2","Private Company"),
    PUBLIC_LAW_BODY("Code-3","Public Law Body");

    companion object {
        fun getValues(): List<String> =
            LegalForm.values().map { it.value }
    }

}