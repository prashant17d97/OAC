package org.prashant.oac.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.debugdesk.notebook.data.util.DataStoreObj

/**
 * Creates a DataStore object for storing data in Android.
 *
 * @param context The context used to access the application's files directory.
 * @return A DataStore object for storing data.
 *
 * @author Prashant Singh
 */
fun dataStore(context: Context): DataStore<Preferences> {
    // Create a DataStore object with the specified file path
    return DataStoreObj.createDataStore(
        // Generate the file path for the DataStore
        dataStoreDirectory = {
            // Resolve the path to the data store file within the application's files directory
            context.filesDir.resolve(DataStoreObj.DATA_STORE_FILE_NAME).absolutePath
        }
    )
}

