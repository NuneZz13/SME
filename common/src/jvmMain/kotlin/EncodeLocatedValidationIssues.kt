@file:JvmName("LocatedValidationIssuesEncoder")

package io.kform.tutorial

import io.kform.LocatedValidationIssue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun encodeLocatedValidationIssues(issues: List<LocatedValidationIssue>): String =
    Json.encodeToString(issues)
