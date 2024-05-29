package pt.at.sme.kotlin.util

import kotlinx.serialization.Serializable
import pt.at.sme.kotlin.priorNotification.PriorNotificationForm
import kotlin.js.JsExport

/**
 * Initial values passed to client-side ImportForm
 */
@Serializable
@JsExport
data class InitialValues(
    val nif: String
//    val mode: FormMode,
//    val preFilledForm: PriorNotificationForm?
)
