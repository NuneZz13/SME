package io.kform.tutorial

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pt.at.sme.kotlin.priorNotification.PriorNotificationForm

@JsExport
fun encodePriorNotificationForm(
    priorNotificationForm: PriorNotificationForm,
    pretty: Boolean = false
): String {
    val formatter = Json { prettyPrint = pretty }
    return formatter.encodeToString(priorNotificationForm)
}
