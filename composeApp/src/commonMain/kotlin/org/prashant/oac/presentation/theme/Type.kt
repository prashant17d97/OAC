package org.prashant.oac.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.opensans_regular
import org.jetbrains.compose.resources.Font


@Composable
private fun robotoCondensedFontFamily() = FontFamily(
    Font(Res.font.opensans_regular, weight = FontWeight.SemiBold)
)

private val baseline = Typography()


val Typography: Typography
    @Composable
    get() {
        val displayFontFamily = robotoCondensedFontFamily()
        return Typography(
            displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
            displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
            displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
            headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
            headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
            headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
            titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
            titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
            titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
            bodyLarge = baseline.bodyLarge.copy(fontFamily = displayFontFamily),
            bodyMedium = baseline.bodyMedium.copy(fontFamily = displayFontFamily),
            bodySmall = baseline.bodySmall.copy(fontFamily = displayFontFamily),
            labelLarge = baseline.labelLarge.copy(fontFamily = displayFontFamily),
            labelMedium = baseline.labelMedium.copy(fontFamily = displayFontFamily),
            labelSmall = baseline.labelSmall.copy(fontFamily = displayFontFamily),
        )
    }
