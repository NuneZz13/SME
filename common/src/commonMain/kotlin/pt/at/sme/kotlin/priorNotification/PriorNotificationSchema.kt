@file:JvmName("PriorNotificationSchemas")

package pt.at.sme.kotlin.priorNotification

import pt.at.sme.kotlin.util.ValidationRegexs
import pt.at.sme.kotlin.enums.MemberState
import io.kform.Schema
import io.kform.schemas.*
import io.kform.validations.*
import pt.at.sme.kotlin.enums.LegalForm
import kotlin.js.JsExport
import kotlin.jvm.JvmName


val identificationSchema = ClassSchema {

    Identification::vatIdentificationNumber {
        ClassSchema (){
            VatIdentificationNumber::issuingCountry { StringSchema(initialValue = MemberState.PORTUGAL.ueCode, validations = listOf(NotEmpty(), OneOf(MemberState.PORTUGAL.ueCode))) }
            VatIdentificationNumber::vatIdNum { StringSchema(validations = listOf(NotEmpty())) }
        }
     }
    Identification::name { StringSchema(validations = listOf(NotEmpty())) }
    Identification::activityType {
        ListSchema(validations = listOf(MinSize(1))) {
            StringSchema()
        }
    }
    Identification::legalForm { StringSchema(validations = listOf(NotEmpty(), OneOf(LegalForm.getValues()))) }
    Identification::hasOSSRegistration {
        BooleanSchema(validations = listOf(
            PriorNotificationValidationsForm.OSSNumberFilledAndCheckBoxHasRegisteredInOSSNotChecked("ossNumberFilledAndCheckBoxHasRegisteredInOSSNotChecked")
        ))
    }

    Identification::ossNumber {
        NullableSchema(validations = listOf(
            PriorNotificationValidationsForm.OSSNumberMandatoryIfHasRegisteredInOSS("ossNumberMandatoryIfHasRegisteredInOSS"))
        ) {
            StringSchema()
        }
    }

}


val contactSchema = ClassSchema {

    Contact::street { StringSchema(validations = listOf(NotEmpty())) }
    Contact::number { StringSchema(validations = listOf(NotEmpty())) }
    Contact::postCode { StringSchema(validations = listOf(NotEmpty())) }
    Contact::city { StringSchema(validations = listOf(NotEmpty())) }
    Contact::country { StringSchema(validations = listOf(NotEmpty(), OneOf(MemberState.getUeCodes()))) }
    Contact::websites {
        NullableSchema {
            TableSchema {
                StringSchema(validations = listOf(NotEmpty(), Matches(regex = ValidationRegexs.website)))
            }
        }
    }
    Contact::phoneNumber {
        NullableSchema {
            StringSchema(validations = listOf(Matches(regex = ValidationRegexs.phoneNumber)))
        }
    }

    Contact::emailAddress {
        NullableSchema {
            StringSchema(validations = listOf(MatchesEmail()))
        }
    }

}


val totalValueOfSuppliesSchema = ClassSchema () {

    TotalValueOfSuppliesModel::memberState { StringSchema(validations = listOf(OneOf(MemberState.getUeCodes()))) }

    TotalValueOfSuppliesModel::totalValue { IntSchema(validations = listOf(Min(0))) }

}


@Suppress("NON_EXPORTABLE_TYPE")
@JsExport
val priorNotificationFormSchema: Schema<PriorNotificationForm> = ClassSchema {

    PriorNotificationForm::identification {
        identificationSchema
    }
    PriorNotificationForm::contact {
        contactSchema
    }

    PriorNotificationForm::memberStatesOfExemption {
        TableSchema(
            validations = listOf(MinSize(1), Unique())
        ) {
            StringSchema (validations = listOf(OneOf(MemberState.getUeCodes())))
        }
    }

    PriorNotificationForm::totalSuppliesCurrCalendar{
        TableSchema(
            validations = listOf(
                MinSize(1),
                UniqueBy<TotalValueOfSuppliesModel, String>(selector = {it.memberState}),
                PriorNotificationValidationsForm.PortugalNotExistsOnTable( code = "portugalNotExistsOnTable")

            )
        ) {
            totalValueOfSuppliesSchema
        }
    }

    PriorNotificationForm::totalSuppliesPrevCalendar{
        NullableSchema {
            TableSchema(
                validations = listOf(
                    UniqueBy<TotalValueOfSuppliesModel, String>(selector = {it.memberState}),
                    PriorNotificationValidationsForm.PortugalNotExistsOnTable( code = "portugalNotExistsOnTable")
                )
            ) {
                totalValueOfSuppliesSchema
            }
        }

    }

    PriorNotificationForm::totalSuppliesPriorYearToPrevCalendar{
        NullableSchema{
            TableSchema(
                validations = listOf(
                    UniqueBy<TotalValueOfSuppliesModel, String>(selector = {it.memberState}),
                    PriorNotificationValidationsForm.PortugalNotExistsOnTable( code = "portugalNotExistsOnTable")
                )
            ){
                totalValueOfSuppliesSchema
            }
        }

    }

    PriorNotificationForm::identifiedInOtherMS{
        BooleanSchema()
    }

}
