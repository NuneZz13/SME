package io.kform.tutorial

import io.kform.LocatedValidationIssue
import kotlinx.serialization.json.Json

@Suppress("NON_EXPORTABLE_TYPE")
@JsExport
fun decodeLocatedValidationIssues(jsonIssues: String): List<LocatedValidationIssue> =
    Json.decodeFromString(jsonIssues)
