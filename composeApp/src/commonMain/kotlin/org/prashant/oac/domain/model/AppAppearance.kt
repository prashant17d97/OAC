package org.prashant.oac.domain.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppAppearance(
    @SerialName("colorContrast")
    val colorContrast: ColorContrast = ColorContrast.LOW,
    @SerialName("themeMode")
    val themeMode: ThemeMode = ThemeMode.System,
    @SerialName("useSystemPalette")
    val useSystemPalette: Boolean = false,
)

enum class ColorContrast(val label: String) {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

enum class ThemeMode(val label: String) {
    System("System Default"), Light("Light"), Dark("Dark");
}

val ThemeMode.isDakTheme: Boolean
    @Composable
    get() = when (this) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }