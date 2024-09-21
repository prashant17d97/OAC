package org.prashant.oac.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.home
import oac.composeapp.generated.resources.search
import oac.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.StringResource
import org.prashant.oac.presentation.navigation.Screens
import org.prashant.oac.util.NavigationEnum

data class NavigationModel(
    val label: StringResource,
    val icon: ImageVector = Icons.Rounded.Home,
    val isSelected: Boolean,
    val route: String,
    val navigationEnum: NavigationEnum
)

val navigationModels = listOf(
    NavigationModel(
        label = Res.string.home,
        icon = Icons.Rounded.Home,
        isSelected = true,
        route = Screens.Home.route,
        navigationEnum = NavigationEnum.Home
    ),
    NavigationModel(
        label = Res.string.search,
        icon = Icons.Rounded.Search,
        isSelected = false,
        route = Screens.Search.route,
        navigationEnum = NavigationEnum.Search
    ),
    NavigationModel(
        label = Res.string.settings,
        icon = Icons.Rounded.Settings,
        isSelected = false,
        route = Screens.Settings.route,
        navigationEnum = NavigationEnum.Settings
    ),
)