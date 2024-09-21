package org.prashant.oac.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Wrapper class for managing data stored within a DataStore instance. It provides functions to
 * retrieve, set, and remove data of various types from the DataStore, as well as checking for the
 * existence of a key and clearing the entire DataStore.
 *
 * @property dataStore The DataStore instance used for data storage.
 *
 * @param dataStore The DataStore instance used for data storage.
 *
 * @constructor Creates an instance of [DataStore] with the provided DataStore instance.
 *
 * @author Prashant Singh
 */
class OacDataStore(private val dataStore: DataStore<Preferences>) {

    /**
     * Retrieves a string value associated with the specified key from the DataStore.
     *
     * @param key The key used to retrieve the string value.
     * @param defaultValue The default value to return if the key is not found.
     * @return A [Flow] emitting the retrieved string value.
     */
    fun getString(
        key: Preferences.Key<String>,
        defaultValue: String = "",
    ): Flow<String> {
        return dataStore.data.map { preferences -> preferences[key] ?: defaultValue }
    }

    /**
     * Sets a value associated with the specified key in the DataStore.
     *
     * @param key The key used to set the value.
     * @param value The value to be set.
     */
    suspend fun <T> set(
        key: Preferences.Key<T>,
        value: T,
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
    /**
     * Clears all data stored in the DataStore.
     */
    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
