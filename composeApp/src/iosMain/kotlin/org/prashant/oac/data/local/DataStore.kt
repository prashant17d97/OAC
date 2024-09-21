package org.prashant.oac.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.debugdesk.notebook.data.util.DataStoreObj
import app.debugdesk.notebook.data.util.DataStoreObj.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Creates a DataStore instance for managing preferences data on the iOS side.
 * @return A DataStore instance for managing preferences.
 * @OptIn Marks the function as experimental, allowing the use of foreign APIs.
 * @param [dataStoreDirectory] A lambda function that produces the path for storing the data store file.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalForeignApi::class)
fun dataStore(): DataStore<Preferences> =
    createDataStore(
        dataStoreDirectory = {
            // Produce the path for storing the data store file...
            val documentDirectory: NSURL? =
                NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
            requireNotNull(documentDirectory).path + "/${DataStoreObj.DATA_STORE_FILE_NAME}"
        },
    )

