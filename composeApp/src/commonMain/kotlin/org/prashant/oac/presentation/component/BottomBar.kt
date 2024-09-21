package org.prashant.oac.presentation.component

import OacTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.prashant.oac.domain.model.NavigationModel
import org.prashant.oac.domain.model.navigationModels

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navigationModels: List<NavigationModel>,
    currentDestination: String?,
    onClick: (NavigationModel) -> Unit,
) {
    NavigationBar {
        navigationModels.forEachIndexed { _, navigationModel ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = navigationModel.icon,
                        contentDescription = stringResource(resource = navigationModel.label)
                    )
                },
                label = { Text(stringResource(resource = navigationModel.label)) },
                selected = navigationModel.route == currentDestination,
                onClick = {
                    onClick(navigationModel)
                },
                modifier = modifier
            )

        }
    }
}

@Preview
@Composable
private fun BottomBarPrev() {
    OacTheme {
        BottomBar(
            navigationModels = navigationModels,
            onClick = {},
            currentDestination = "currentDestination",
        )
    }
}


