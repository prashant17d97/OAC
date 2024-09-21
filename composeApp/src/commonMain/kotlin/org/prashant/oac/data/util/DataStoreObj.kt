package app.debugdesk.notebook.data.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.debugdesk.notebook.data.util.DataStoreObj.DATA_STORE_FILE_NAME
import okio.Path.Companion.toPath

/**
 * Object responsible for creating a DataStore instance with the specified name and path.
 * It provides a function to create a DataStore with a custom path using the PreferenceDataStoreFactory.
 *
 * @property DATA_STORE_FILE_NAME The name of the DataStore file.
 *
 * @see DataStore
 * @see Preferences
 * @see PreferenceDataStoreFactory
 *
 * @constructor Creates an instance of [DataStoreObj].
 *
 * @author Prashant Singh
 */
object DataStoreObj {
    const val DATA_STORE_FILE_NAME = "NotebookDataStore.preferences_pb"

    /**
     * Creates a DataStore instance with a custom path using the PreferenceDataStoreFactory.
     *
     * @param dataStoreDirectory A function that produces the path where the DataStore file will be created.
     * @return The created DataStore instance.
     */
    fun createDataStore(dataStoreDirectory: () -> String): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = null,
            migrations = emptyList(),
            produceFile = { dataStoreDirectory().toPath() },
        )
}

