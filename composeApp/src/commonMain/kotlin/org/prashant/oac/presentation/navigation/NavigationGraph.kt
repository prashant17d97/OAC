package org.prashant.oac.presentation.navigation

import OacTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.painterResource
import org.prashant.oac.MainVM
import org.prashant.oac.domain.model.AppDetails
import org.prashant.oac.domain.model.NavigationModel
import org.prashant.oac.domain.model.isDakTheme
import org.prashant.oac.domain.model.navigationModels
import org.prashant.oac.presentation.HomePage
import org.prashant.oac.presentation.Search
import org.prashant.oac.presentation.Settings
import org.prashant.oac.presentation.component.BottomBar

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    mainVM: MainVM,
) {
    var navigationModels by remember { mutableStateOf(navigationModels) }
    val appDetails by mainVM.apps.collectAsState()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination by rememberUpdatedState(navBackStackEntry?.destination?.route)
    val appearance by mainVM.appAppearance.collectAsState()

    OacTheme(
        colorContrast = appearance.colorContrast,
        darkTheme = appearance.themeMode.isDakTheme,
        dynamicColor = appearance.useSystemPalette
    ) {
        NavigationGraphContainer(
            navHostController = navHostController,
            appDetails = appDetails,
            currentDestination = currentDestination,
            navigationModels = navigationModels,
            onNavigationClick = { bottomNavigation ->
                navigationModels = navigationModels.map { navigationModel ->
                    navigationModel.copy(isSelected = navigationModel == bottomNavigation)
                }
                navHostController.navigate(bottomNavigation.route)
            },
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationGraphContainer(
    modifier: Modifier = Modifier,
    currentDestination: String?,
    navHostController: NavHostController,
    appDetails: List<AppDetails>,
    navigationModels: List<NavigationModel>,
    onNavigationClick: (NavigationModel) -> Unit,
) {
    Scaffold(modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface, topBar = {
            Surface(
                tonalElevation = 4.dp, shadowElevation = 4.dp
            ) {
                TopAppBar(
                    modifier = modifier,
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                space = 4.dp, alignment = Alignment.CenterHorizontally
                            ), verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navHostController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_back),
                                    contentDescription = "Navigate"
                                )
                            }
                            Text(
                                text = "OB App Controller",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    },
                )
            }
        },

        bottomBar = {
            BottomBar(
                navigationModels = navigationModels,
                currentDestination = currentDestination,
                onClick = onNavigationClick
            )
        }) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding).padding(16.dp),
            navController = navHostController,
            startDestination = Screens.Home.route
        ) {
            composable(route = Screens.Home.route) {
                HomePage(appDetails = appDetails)
            }
            composable(route = Screens.Search.route) {
                Search()
            }
            composable(route = Screens.Settings.route) {
                Settings()
            }
        }
    }
}