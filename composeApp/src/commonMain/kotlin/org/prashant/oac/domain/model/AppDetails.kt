package org.prashant.oac.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.no
import oac.composeapp.generated.resources.yes
import org.jetbrains.compose.resources.StringResource

data class AppDetails(
    val appControlButtons: List<AppButtons>,
    val deepLinkScript: List<String>,
    val icon: ImageBitmap?=null,
    val isDebug: Boolean,
    val name: String,
    val packageName: String,
    val releaseType: StringResource,
    val version: String,
    val versionCode: Long,
) {
    val debuggable: StringResource = if (isDebug) {
        Res.string.yes
    } else {
        Res.string.no
    }
}