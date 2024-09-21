package org.prashant.oac.presentation.navigation

sealed class Screens(val route: String) {
    data object Home : Screens(NavigationScreens.HOME)
    data object Search : Screens(NavigationScreens.SEARCH)
    data object Settings : Screens(NavigationScreens.SETTINGS)
}
