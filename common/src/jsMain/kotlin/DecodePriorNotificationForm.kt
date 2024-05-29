package io.kform.tutorial

import kotlinx.serialization.json.Json
import pt.at.sme.kotlin.priorNotification.PriorNotificationForm

@JsExport
fun decodePriorNotificationForm(priorNotificationForm: String): PriorNotificationForm =
    Json.decodeFromString(priorNotificationForm)
