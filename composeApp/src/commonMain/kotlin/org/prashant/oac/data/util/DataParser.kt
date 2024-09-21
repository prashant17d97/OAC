package app.debugdesk.notebook.data.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DataParser {

    val json = Json { encodeDefaults = true }

    /**
     * Determines if a string is empty or null.
     * @return The original string if not empty or null, otherwise returns null.
     */

    fun String?.isDataEmpty(): String? {
        // Check if the string is empty or null...
        return if (this.isNullOrBlank()) {
            null
        } else {
            this
        }
    }

    inline fun <reified Generic> String.toData(): Generic {
        return json.decodeFromString<Generic>(this)
    }

    inline fun <reified Generic> Generic.toJsonString(): String {
        return json.encodeToString(this)
    }
}