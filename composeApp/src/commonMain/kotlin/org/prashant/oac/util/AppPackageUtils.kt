package org.prashant.oac.util

import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.external
import oac.composeapp.generated.resources.internal
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.readResourceBytes
import org.koin.core.component.KoinComponent
import org.prashant.oac.domain.model.App
import org.prashant.oac.domain.model.AppButtons
import org.prashant.oac.domain.model.AppControlButton
import org.prashant.oac.domain.model.AppDetails
import org.prashant.oac.domain.model.AppPackages
import org.prashant.oac.util.Resources.toDecodedString

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppPackageUtils : KoinComponent {

    suspend fun getAppPackages(): List<AppDetails>

    fun getAppDetails(app: App): AppDetails?
    @Composable
    fun launchApp(scriptLink: String)
}

object AppHelperUtils {

    val String.releaseType: StringResource
        get() = if (this.contains("internal", ignoreCase = true)) {
            Res.string.internal
        } else {
            Res.string.external
        }

    @OptIn(InternalResourceApi::class)
    suspend fun fetchAppLists(fileName: String, onError: (Exception) -> Unit = {}): List<App>? {
        return try {
            val json = readResourceBytes(fileName).toDecodedString()
            println("PackageJson: $json")
            parseAppJson(json)

        } catch (ex: Exception) {
            onError(ex)
            null
        }
    }


    private fun parseAppJson(jsonString: String?): List<App>? {
        val apps = jsonString?.let { Json.decodeFromString<AppPackages>(it).apps }
        return apps
    }

    fun parseSavedAppJson(jsonString: String?): List<App>? {
        val apps = jsonString?.let { Json.decodeFromString<AppPackages>(it).apps }
        return apps
    }


    private fun AppControlButton.getScriptLink(packageName: String): String {
        return if (action.isNotEmpty()) {
            "https://$packageName/$action"
        } else {
            "https://$packageName/"
        }
    }

    fun App.getScriptLinks(): List<String> {
        return appControlButtons.map { it.getScriptLink(packageName) }
    }

    fun App.getAppButtons(): List<AppButtons> {
        return appControlButtons.map {
            AppButtons(
                action = it.action, text = it.text, scriptLink = it.getScriptLink(packageName)
            )
        }
    }
}