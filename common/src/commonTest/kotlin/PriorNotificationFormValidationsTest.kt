import io.kform.FormValidator
import io.kform.LocatedValidationError
import io.kform.datatypes.toTable
import io.kform.test.assertMatchingIssues
import kotlinx.coroutines.test.runTest
import pt.at.sme.kotlin.priorNotification.*
import kotlin.test.Test

class PriorNotificationFormValidationsTest {

    private val validFormValue =
        PriorNotificationForm(
            //Identification
            Identification(
                vatIdentificationNumber = VatIdentificationNumber("PT","501303260"),
                name = "Personal Tax Number",
                activityType = listOf("4961"),
                legalForm = "Natural Person",
                hasOSSRegistration = false,
                ossNumber = ""
            ),
            //Contact
            Contact(
                street = "rua",
                number = "numero",
                postCode = "1070-103",
                city = "Lisboa",
                country = "PT",
                websites = null,
                phoneNumber = "213804410",
                emailAddress = "geral@opensoft.pt"
            ),
            //Estados Membros de Isenção
            listOf("ES").toTable(),
            //totalSuppliesCurrCalendar
            listOf(
                TotalValueOfSuppliesModel("ES", 50000),
                TotalValueOfSuppliesModel("PT", 50000)
            ).toTable(),
            //totalSuppliesPrevCalendar
            listOf(
                TotalValueOfSuppliesModel("ES", 50000),
                TotalValueOfSuppliesModel("PT", 50000)

            ).toTable(),
            //totalSuppliesPriorYearToPrevCalendar
            listOf(
                TotalValueOfSuppliesModel("ES", 50000),
                TotalValueOfSuppliesModel("PT", 50000)
            ).toTable(),
            //identifiedInOtherMS
            true

        )

    private val emptyFormValue =
        PriorNotificationForm(
            Identification(
                vatIdentificationNumber = VatIdentificationNumber("",""),
                name = "",
                activityType = emptyList(),
                legalForm = "",
                hasOSSRegistration = false,
                ossNumber = ""
            ),
            Contact(
                street = "",
                number = "",
                postCode = "",
                city = "",
                country = "",
                websites = null,
                phoneNumber = null,
                emailAddress = null
            ),
            emptyList<String>().toTable(),
            emptyList<TotalValueOfSuppliesModel>().toTable(),
            emptyList<TotalValueOfSuppliesModel>().toTable(),
            emptyList<TotalValueOfSuppliesModel>().toTable(),
            false
        )


    @Test
    fun validForm_success() = runTest {
        val validator = FormValidator(priorNotificationFormSchema)

        assertMatchingIssues(listOf(), validator.validate(validFormValue))
    }


    @Test
    fun emptyForm_failure() = runTest {
        val validator = FormValidator(priorNotificationFormSchema)

        assertMatchingIssues(
            listOf(
                /* Identification */
                LocatedValidationError("/identification/vatIdentificationNumber/issuingCountry", "notEmpty"),
                LocatedValidationError("/identification/vatIdentificationNumber/issuingCountry", "disallowedValue"),
                LocatedValidationError("/identification/vatIdentificationNumber/vatIdNum", "notEmpty"),
                LocatedValidationError("/identification/name", "notEmpty"),
                LocatedValidationError("/identification/activityType", "minSize"),
                LocatedValidationError("/identification/legalForm", "notEmpty"),
                LocatedValidationError("/identification/legalForm", "disallowedValue"),

                /* Contact */
                LocatedValidationError("/contact/street", "notEmpty"),
                LocatedValidationError("/contact/number", "notEmpty"),
                LocatedValidationError("/contact/postCode", "notEmpty"),
                LocatedValidationError("/contact/city", "notEmpty"),
                LocatedValidationError("/contact/country", "notEmpty"),
                LocatedValidationError("/contact/country", "disallowedValue"),

                /* memberStatesOfExemption */
                LocatedValidationError("/memberStatesOfExemption", "minSize"),
                /* totalSuppliesCurrCalendar */
                LocatedValidationError("/totalSuppliesCurrCalendar", "minSize"),


                ),
            validator.validate(emptyFormValue)
        )
    }

    /**
     * Validar número VAT Identification Number
     */
    @Test
    fun validateIfissuingCountryOfVATIdentificationNumberIsPT() = runTest {
        val validator = FormValidator(priorNotificationFormSchema)
        val formValue =
            validFormValue.copy(identification =
                    validFormValue.identification.copy(vatIdentificationNumber =
                        validFormValue.identification.vatIdentificationNumber.copy(issuingCountry = "XI")));

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/identification/vatIdentificationNumber/issuingCountry", "disallowedValue"),
            ),
            validator.validate(formValue)
        )
    }

    /**
     * Validar que quando é selecionado o "pisco" que está registado no regime união do OSS que preenche o Número OSS.
     */
    @Test
    fun validateOSSNumberMandatoryIfHasRegisteredInOSS() = runTest {
        val validator = FormValidator(priorNotificationFormSchema)
        val formValue = validFormValue.copy(identification =
                    validFormValue.identification.copy(hasOSSRegistration = true, ossNumber = ""))

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/identification/ossNumber", "ossNumberMandatoryIfHasRegisteredInOSS"),
            ),
            validator.validate(formValue)
        )
    }

    /**
     * Validar que quando o número OSS é preenchido que também é selecionado o "pisco" que está registado no regime união do OSS.
     */
    @Test
    fun validateOSSNumberFilledAndCheckBoxHasRegisteredInOSSNotChecked() = runTest {
        val validator = FormValidator(priorNotificationFormSchema)

        val formValue = validFormValue.copy(
            identification =
            validFormValue.identification.copy(hasOSSRegistration = false, ossNumber = "501303260")
        )

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/identification/hasOSSRegistration", "ossNumberFilledAndCheckBoxHasRegisteredInOSSNotChecked"),
            ),
            validator.validate(formValue)
        )
    }

    /**
     * Validar que o país da morada pertence à lista de estados membros
     */
    @Test
    fun validateIfCountryIsInMemberStateList() = runTest {
        val validator = FormValidator(priorNotificationFormSchema)

        val formValue = validFormValue.copy(
            contact = validFormValue.contact.copy(country = "XI")
        )

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/contact/country", "disallowedValue"),
            ),
            validator.validate(formValue)
        )
    }

    /** Validar que o URL do Website é valido */
    @Test
    fun validateWebsiteURL() = runTest{
        val validator = FormValidator(priorNotificationFormSchema)

        val formValue = validFormValue.copy(
            contact = validFormValue.contact.copy(websites = listOf("website").toTable())
        )

        assertMatchingIssues(
        listOf(
        LocatedValidationError("/contact/websites/0", "match"),
        ),
        validator.validate(formValue)
        )
    }

    /**
     * Validar que o email é válido.
     */
    @Test
    fun testEmailIsValid() = runTest {
        val validator = FormValidator(priorNotificationFormSchema)
        val formValue =
            validFormValue.copy(contact = validFormValue.contact.copy(emailAddress = "notValidEmail"))

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/contact/emailAddress", "invalidEmail"),
            ),
            validator.validate(formValue)
        )
    }

    /**
     * Validar que o número de telefone é úm número válido.
     */
    @Test
    fun testPhoneNumberIsValid() = runTest {
        val validator = FormValidator(priorNotificationFormSchema)
        val formValue =
            validFormValue.copy(contact = validFormValue.contact.copy(phoneNumber = "123456789A0"))

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/contact/phoneNumber", "match"),
            ),
            validator.validate(formValue)
        )
    }

    /**
     * Validar que não se podem repetir estados membros na lista de estados membros de isenção.
     */
    @Test
    fun repeatedElementInMemberStateOfExemption() = runTest(){
        val validator = FormValidator(priorNotificationFormSchema)
        val formValue =
            validFormValue.copy(memberStatesOfExemption = listOf("ES", "NL", "ES").toTable())

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/memberStatesOfExemption", "repeatedElements"),
            ),
            validator.validate(formValue)
        )
    }

    /**
     * Validar que o valor dos totais de fornecimentos por estados membros não pode ser inferior a 0.
     */
    @Test
    fun totalOfSuppliesCanNotBeNegative() = runTest(){
        val validator = FormValidator(priorNotificationFormSchema)
        val totalValueOfSuppliesModelWithNegativeValue = listOf(
            TotalValueOfSuppliesModel("PT", 200),
            TotalValueOfSuppliesModel("ES", -1200)).toTable()


        val formValue = validFormValue
                .copy(totalSuppliesCurrCalendar = totalValueOfSuppliesModelWithNegativeValue)
                .copy(totalSuppliesPrevCalendar = totalValueOfSuppliesModelWithNegativeValue)
                .copy(totalSuppliesPriorYearToPrevCalendar = totalValueOfSuppliesModelWithNegativeValue)

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/totalSuppliesCurrCalendar/1/totalValue", "min"),
                LocatedValidationError("/totalSuppliesPrevCalendar/1/totalValue", "min"),
                LocatedValidationError("/totalSuppliesPriorYearToPrevCalendar/1/totalValue", "min"),
            ),
            validator.validate(formValue)
        )
    }

    /**
     * Validar que não pode existir mais do que um fornecimentos para o mesmo estado membro
     */
    @Test
    fun validateThatTotalsJustHaveOneEntryForMemberState() = runTest(){
        val validator = FormValidator(priorNotificationFormSchema)
        val totalValueOfSuppliesModelWithNegativeValue = listOf(
                TotalValueOfSuppliesModel("PT", 200),
                TotalValueOfSuppliesModel("ES", 200),
                TotalValueOfSuppliesModel("ES", 100)
            ).toTable()


        val formValue = validFormValue
            .copy(totalSuppliesCurrCalendar = totalValueOfSuppliesModelWithNegativeValue)
            .copy(totalSuppliesPrevCalendar = totalValueOfSuppliesModelWithNegativeValue)
            .copy(totalSuppliesPriorYearToPrevCalendar = totalValueOfSuppliesModelWithNegativeValue)

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/totalSuppliesCurrCalendar", "repeatedElements"),
                LocatedValidationError("/totalSuppliesPrevCalendar", "repeatedElements"),
                LocatedValidationError("/totalSuppliesPriorYearToPrevCalendar", "repeatedElements"),
            ),
            validator.validate(formValue)
        )
    }

    /**
     * Validar que se existir fornecimentos, que existe fornecimentos para Portugal.
     */
    @Test
    fun validateThatExistSuppliesForPortugal() = runTest(){
        val validator = FormValidator(priorNotificationFormSchema)
        val totalValueOfSuppliesModelWithNegativeValue = listOf(
            TotalValueOfSuppliesModel("ES", 200)
        ).toTable()


        val formValue = validFormValue
            .copy(totalSuppliesCurrCalendar = totalValueOfSuppliesModelWithNegativeValue)
            .copy(totalSuppliesPrevCalendar = totalValueOfSuppliesModelWithNegativeValue)
            .copy(totalSuppliesPriorYearToPrevCalendar = totalValueOfSuppliesModelWithNegativeValue)

        assertMatchingIssues(
            listOf(
                LocatedValidationError("/totalSuppliesCurrCalendar", "portugalNotExistsOnTable"),
                LocatedValidationError("/totalSuppliesPrevCalendar", "portugalNotExistsOnTable"),
                LocatedValidationError("/totalSuppliesPriorYearToPrevCalendar", "portugalNotExistsOnTable"),
            ),
            validator.validate(formValue)
        )
    }



}
