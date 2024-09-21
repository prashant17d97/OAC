package org.prashant.oac

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.prashant.oac.presentation.navigation.NavigationGraph

@Composable
@Preview
fun App() {
    val mainVM: MainVM = koinInject()
    NavigationGraph(
        navHostController = rememberNavController(),
        mainVM = mainVM
    )
}