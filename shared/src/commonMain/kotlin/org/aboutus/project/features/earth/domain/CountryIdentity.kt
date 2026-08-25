package org.aboutus.project.features.earth.domain

data class CountryIdentity(
    val code: String,
    val name: String
)

/**
 * The app uses one stable identity for Palestine across the UI, scope requests,
 * messages, and statistics regardless of a source dataset's legacy label.
 */
fun Country.toAppCountryIdentity(): CountryIdentity {
    val sourceCode = id.substringBefore("_").toAppCountryCode()
    return if (sourceCode == PALESTINE_CODE || name.contains("israel", ignoreCase = true)) {
        CountryIdentity(code = PALESTINE_CODE, name = PALESTINE_NAME)
    } else {
        CountryIdentity(code = sourceCode, name = name)
    }
}

fun String.toAppCountryCode(): String = when (trim().uppercase()) {
    "IL", "ISR", "ISRAEL", "PSE", "PALESTINE", PALESTINE_CODE -> PALESTINE_CODE
    else -> trim().uppercase()
}

const val PALESTINE_CODE = "PS"
const val PALESTINE_NAME = "Palestine"
