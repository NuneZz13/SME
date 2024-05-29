package pt.at.sme.kotlin.priorNotification

import io.kform.Validation
import io.kform.ValidationContext
import io.kform.ValidationError
import io.kform.ValidationIssue
import io.kform.datatypes.Table
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pt.at.sme.kotlin.enums.MemberState

class PriorNotificationValidationsForm {

    /** If is selected hasOSSRegistration, then ossNumber should be filled */
    class OSSNumberMandatoryIfHasRegisteredInOSS(private val code: String) : Validation<String?>() {

        private val ValidationContext.hasOSSRegistration by dependency<Boolean>("../hasOSSRegistration")

        override fun ValidationContext.validate(): Flow<ValidationIssue> = flow {

            if (hasOSSRegistration && value.isNullOrEmpty()) {
                emit(ValidationError(code))
            }

        }
    }

    /** If ossNumber is filled, then hasOSSRegistration need to be selected   */
    class OSSNumberFilledAndCheckBoxHasRegisteredInOSSNotChecked(private val code: String) : Validation<Boolean>() {

        private val ValidationContext.ossNumber by dependency<String?>("../ossNumber")

        override fun ValidationContext.validate(): Flow<ValidationIssue> = flow {

            if (!value && !ossNumber.isNullOrEmpty()) {
                emit(ValidationError(code))
            }

        }
    }

    /**
     * Validates if the table contains information for Portugal.
     */
    class PortugalNotExistsOnTable(private val code: String) : Validation<Table<TotalValueOfSuppliesModel>>() {
        override val dependsOnDescendants = true

        override fun ValidationContext.validate(): Flow<ValidationIssue> = flow {
            if (!value.isEmpty() && value.values.none {
                    it.memberState.isNotBlank() && it.memberState == MemberState.PORTUGAL.a2Code
                }) {
                emit(ValidationError(code))
            }
        }
    }
}