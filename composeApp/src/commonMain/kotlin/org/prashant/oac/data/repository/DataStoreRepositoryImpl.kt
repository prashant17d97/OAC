package org.prashant.oac.data.repository

import androidx.datastore.preferences.core.stringPreferencesKey
import app.debugdesk.notebook.data.util.DataParser.toData
import app.debugdesk.notebook.data.util.DataParser.toJsonString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import org.prashant.oac.data.local.OacDataStore
import org.prashant.oac.domain.model.App
import org.prashant.oac.domain.model.AppAppearance
import org.prashant.oac.domain.model.AppControlButton
import org.prashant.oac.domain.model.AppDetails
import org.prashant.oac.domain.model.AppPackages
import org.prashant.oac.domain.repositories.DataStoreRepository
import org.prashant.oac.util.AppHelperUtils.parseSavedAppJson
import org.prashant.oac.util.AppPackageUtils.getAppDetails

/**
 * Implements the [DataStoreRepository] interface to manage data stored within the application using DataStore.
 * It handles operations such as saving and retrieving posts, updating the application theme, and managing
 * the system palette preference.
 *
 * @param oacDataStore The DataStore instance used for data storage.
 *
 * @constructor Creates an instance of [DataStoreRepositoryImpl] with the provided DataStore instance.
 *
 * @author Prashant Singh
 */
class DataStoreRepositoryImpl(private val oacDataStore: OacDataStore) : DataStoreRepository {

    private val _appAppearance: MutableStateFlow<AppAppearance> = MutableStateFlow(AppAppearance())
    override val appAppearance: StateFlow<AppAppearance> = _appAppearance

    private val _apps: MutableStateFlow<List<AppDetails>> = MutableStateFlow(emptyList())
    override val apps: StateFlow<List<AppDetails>> = _apps

    override suspend fun saveAppAppearance(appAppearance: AppAppearance) {
        oacDataStore.set(APP_APPEARANCE, appAppearance.toJsonString())
        _appAppearance.tryEmit(appAppearance)
    }

    override suspend fun fetchAppearance() {
        val appAppearanceString = oacDataStore.getString(APP_APPEARANCE).firstOrNull()

        val retrievedAppAppearance = appAppearanceString?.let {
            try {
                it.toData<AppAppearance>()
            } catch (e: Exception) {
                println("DataStoreRepositoryImpl:--> ${e.message.toString()}")
                AppAppearance()
            }
        } ?: AppAppearance()

        _appAppearance.tryEmit(retrievedAppAppearance)
    }

    override suspend fun saveNewApp(app: App): Boolean {
        val findApp = apps.value.any { it.packageName == app.packageName }
        if (findApp) return false

        oacDataStore.set(APP_PACKAGES, AppPackages(apps = listOf(app)).toJsonString())
        val apps =
            (getAppDetails(app)?.let { listOf(it) + apps.value } ?: apps.value).sortedBy { it.name }
        _apps.tryEmit(apps)
        return true
    }

    override suspend fun fetchAllApp(appPackages: List<AppDetails>) {
        val appAppearanceString = oacDataStore.getString(APP_PACKAGES).firstOrNull()
        println("DataStoreRepositoryImpl, fetchAllApp: $appAppearanceString")
        // Parse saved apps and map to AppDetails, or use an empty list if not present
        val savedAppDetails = if (!appAppearanceString.isNullOrEmpty()) {
            parseSavedAppJson(appAppearanceString)?.mapNotNull { appDetails ->
                getAppDetails(
                    appDetails
                )
            } ?: emptyList()
        } else {
            emptyList()
        }

        // Use a LinkedHashMap to avoid duplicates while preserving order
        val appMap = linkedMapOf<String, AppDetails>()

        // First, add saved apps to the map (keyed by packageName to avoid duplicates)
        savedAppDetails.forEach { appMap[it.packageName] = it }

        // Then, add new apps (overwrite if they already exist in the map)
        appPackages.forEach { appMap[it.packageName] = it }

        // Convert map values to a list and sort by name
        val allAppDetails = appMap.values.sortedBy { it.name }

        // Emit the updated list
        _apps.tryEmit(allAppDetails)
    }


    override suspend fun deleteApp(app: App) {
        val isAppAvailable = apps.value.any { it.packageName == app.packageName }

        if (app.packageName.isEmpty() && !isAppAvailable) return

        val apps = apps.value.filter { it.packageName != app.packageName }

        val appPackages = AppPackages(apps = apps.map {
            App(packageName = it.packageName,
                appControlButtons = apps.first { appDetails ->
                    appDetails.packageName == it.packageName
                }.appControlButtons.map { appButtons ->
                    AppControlButton(
                        action = appButtons.action,
                        text = appButtons.text,
                    )
                })
        }).toJsonString()
        oacDataStore.set(APP_PACKAGES, appPackages)

        println("DataStoreRepositoryImpl, deleteApp: $app\npackage: $appPackages")

        fetchAllApp(emptyList())
    }


    companion object {
        private val APP_APPEARANCE = stringPreferencesKey("APP_APPEARANCE")
        private val APP_PACKAGES = stringPreferencesKey("APP_PACKAGES")
    }
}
