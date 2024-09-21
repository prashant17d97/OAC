package org.prashant.oac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.prashant.oac.domain.model.App
import org.prashant.oac.domain.repositories.DataStoreRepository
import org.prashant.oac.util.AppPackageUtils.getAppPackages

class MainVM(private val dataStoreRepository: DataStoreRepository) : ViewModel() {
    val appAppearance = dataStoreRepository.appAppearance
    val apps = dataStoreRepository.apps

    init {
        viewModelScope.launch {
            dataStoreRepository.fetchAppearance()
            dataStoreRepository.fetchAllApp(getAppPackages())
        }
    }

    fun saveNewApp(app: App){
        viewModelScope.launch {
            dataStoreRepository.saveNewApp(app)
        }
    }
    fun deleteApp(app: App){
        viewModelScope.launch {
            dataStoreRepository.deleteApp(app)
        }
    }
}