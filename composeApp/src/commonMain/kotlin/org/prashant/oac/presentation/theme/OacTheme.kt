import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import org.prashant.oac.domain.model.ColorContrast

/**
 * Defines the theme for the application, handling color, typography, and other theme-related properties
 * using Material3 styling structure.
 * This function is marked as `expect`, indicating that it must be implemented separately
 * for both Android and iOS platforms.
 * @param darkTheme Whether the theme should be dark or light mode.
 * @param dynamicColor Whether dynamic colors should be applied based on the system's theme.
 * @param content The content of the application to be styled by this theme.
 */
@Composable
expect fun OacTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    colorContrast: ColorContrast = ColorContrast.MEDIUM,
    content: @Composable () -> Unit,
)