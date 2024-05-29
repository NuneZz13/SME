package pt.at.sme.kotlin.util

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * Registration form modes.
 */
@Serializable
@JsExport
data class FormMode(val new: Boolean = false, val edit: Boolean = false, val detail: Boolean = false) {
    companion object {
        val NEW = FormMode(new = true)
        val EDIT = FormMode(edit = true)
        val DETAIL = FormMode(detail = true)
    }
}
