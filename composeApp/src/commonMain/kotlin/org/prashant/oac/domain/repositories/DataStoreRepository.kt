package org.prashant.oac.domain.repositories

import kotlinx.coroutines.flow.StateFlow
import org.prashant.oac.domain.model.App
import org.prashant.oac.domain.model.AppAppearance
import org.prashant.oac.domain.model.AppDetails

/**
 * Represents a repository interface for managing data stored within the application, including posts,
 * application theme, and system palette preference.
 *
 * @author Prashant Singh
 */
interface DataStoreRepository {
    val appAppearance: StateFlow<AppAppearance>

    val apps: StateFlow<List<AppDetails>>

    suspend fun saveAppAppearance(appAppearance: AppAppearance)

    suspend fun fetchAppearance()

    suspend fun saveNewApp(app: App): Boolean

    suspend fun fetchAllApp(appPackages: List<AppDetails>)

    suspend fun deleteApp(app: App)
}

