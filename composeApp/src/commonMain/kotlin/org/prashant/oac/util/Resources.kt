package org.prashant.oac.util

/**
 * An object defining methods for accessing resources.
 *
 * @property getJsonResource A function to retrieve a JSON resource from a byte array and return it as a string.
 *
 *
 * @author Prashant Singh
 */
expect object Resources {

    /**
     * Retrieves a JSON resource from a byte array and returns it as a string.
     *
     * @param byteArray The byte array containing the JSON resource.
     * @return The JSON resource as a string.
     */
    fun ByteArray.toDecodedString(): String
}
