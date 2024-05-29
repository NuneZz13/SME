package pt.at.sme.kotlin.enums

import kotlin.js.JsExport

@JsExport
enum class MemberState(val ueCode: String, val n3Code: String, val a2Code: String = ueCode) {
    AUSTRIA("AT", "040"),
    BELGIUM("BE", "056"),
    BULGARIA("BG", "100"),
    CYPRUS("CY", "196"),
    CZECHIA("CZ", "203"),
    DENMARK("DK", "208"),
    ESTONIA("EE", "233"),
    FINLAND("FI", "246"),
    FRANCE("FR", "250"),
    GERMANY("DE", "276"),
    GREECE("EL", "300", "GR"),
    HUNGARY("HU", "348"),
    CROATIA("HR", "191"),
    IRELAND("IE", "372"),
    ITALY("IT", "380"),
    LATVIA("LV", "428"),
    LITHUANIA("LT", "440"),
    LUXEMBOURG("LU", "442"),
    MALTA("MT", "470"),
    NETHERLANDS("NL", "528"),
    POLAND("PL", "616"),
    PORTUGAL("PT", "620"),
    ROMANIA("RO", "642"),
    SLOVAKIA("SK", "703"),
    SLOVENIA("SI", "705"),
    SPAIN("ES", "724"),
    SWEDEN("SE", "752");

    companion object {
        fun getUeCodes(): List<String> =
            MemberState.values().map { it.ueCode }
        fun findByUe(ueCode: String): MemberState? =
            values().find { it.ueCode == ueCode }
        fun findByA2(a2Code: String): MemberState? =
            values().find { it.a2Code == a2Code }
        fun findByN3(n3Code: String): MemberState? =
            values().find { it.n3Code == n3Code }
    }
}
