
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.prashant.oac.domain.model.ColorContrast
import org.prashant.oac.presentation.theme.Typography
import org.prashant.oac.presentation.theme.darkColorScheme
import org.prashant.oac.presentation.theme.lightColorScheme

/**
 * Defines the theme for the application, handling color, typography, and other theme-related properties
 * using Material3 styling structure.
 * This function is marked as `expect`, indicating that it must be implemented separately
 * for both Android and iOS platforms.
 * @param darkTheme Whether the theme should be dark or light mode.
 * @param dynamicColor Whether dynamic colors should be applied based on the system's theme.
 * @param applyStaticColor Whether to apply static colors regardless of system theme.
 * @param content The content of the application to be styled by this theme.
 */
@Composable
actual fun OacTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    colorContrast: ColorContrast,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> colorContrast.darkColorScheme
        else -> colorContrast.lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
