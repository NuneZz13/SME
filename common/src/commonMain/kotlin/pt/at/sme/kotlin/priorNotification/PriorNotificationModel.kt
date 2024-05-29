@file:Suppress("NON_EXPORTABLE_TYPE")

package pt.at.sme.kotlin.priorNotification

import pt.at.sme.kotlin.enums.MemberState
import io.kform.collections.MutablePathMultimap
import io.kform.datatypes.Table
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class VatIdentificationNumber(
    var issuingCountry: String,
    var vatIdNum: String,
)

@JsExport
@Serializable
data class Identification(
    var vatIdentificationNumber: VatIdentificationNumber,
    var name: String,
    var activityType: List<String>,
    var legalForm: String,
    var hasOSSRegistration: Boolean,
    var ossNumber: String?
)

@JsExport
@Serializable
data class Contact(
    var street: String,
    var number: String,
    var postCode: String,
    var city: String,
    var country: String,
    var websites: Table<String>?,
    var phoneNumber: String?,
    var emailAddress: String?,
)

@JsExport
@Serializable
data class TotalValueOfSuppliesModel(
    var memberState: String,
    var totalValue: Int,
)


@JsExport
@Serializable
data class PriorNotificationForm(
    var identification: Identification,
    var contact: Contact,
    var memberStatesOfExemption: Table<String>,
    var totalSuppliesCurrCalendar: Table<TotalValueOfSuppliesModel>,
    var totalSuppliesPrevCalendar: Table<TotalValueOfSuppliesModel>?,
    var totalSuppliesPriorYearToPrevCalendar: Table<TotalValueOfSuppliesModel>?,
    var identifiedInOtherMS: Boolean
)






