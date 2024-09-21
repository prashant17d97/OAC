package org.prashant.oac.presentation.component

import OacTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.add
import oac.composeapp.generated.resources.add_new_app
import oac.composeapp.generated.resources.add_new_app_desc
import oac.composeapp.generated.resources.cancel
import oac.composeapp.generated.resources.configuration
import oac.composeapp.generated.resources.delete
import oac.composeapp.generated.resources.environment
import oac.composeapp.generated.resources.launch
import oac.composeapp.generated.resources.no_app_found
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.prashant.oac.domain.model.App
import org.prashant.oac.domain.model.AppControlButton
import org.prashant.oac.domain.model.AppDetails

@Composable
fun AppCardList(
    modifier: Modifier = Modifier,
    appDetails: List<AppDetails>,
    onClick: (scriptLink: String) -> Unit = {},
    onCopyClick: (String) -> Unit = {},
    onAddClick: (App) -> Unit = {},
    onDelete: (App) -> Unit = {},
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var packageName by remember { mutableStateOf("") }

    AnimatedVisibility(visible = appDetails.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(resource = Res.string.no_app_found))
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(appDetails) {
            AppCard(
                modifier = Modifier,
                appDetail = it,
                onClick = onClick,
                onCopyClick = onCopyClick,
                onAddClick = {
                    showAddDialog = true
                    packageName = it.packageName
                },
                onDelete = {
                    showDeleteDialog = true
                    packageName = it.packageName
                }
            )
        }
    }
    AnimatedVisibility(
        visible = showAddDialog && packageName.isNotEmpty(),
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
    ) {
        AddNewApp(
            app = App(packageName = packageName),
            onDismiss = {
                showAddDialog = false
                packageName = ""
            },
            onAddNewApp = {
                onAddClick(it)
                showAddDialog = false
                packageName = ""
            }
        )
    }


    AnimatedVisibility(
        visible = showDeleteDialog,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
    ) {
        DeleteAlert(
            onDismiss = {
                showDeleteDialog = false
                packageName = ""
            },
            onConfirm = {
                onDelete(App(packageName = packageName))
                showDeleteDialog = false
                packageName = ""
            }
        )
    }

}

@Composable
fun DeleteAlert(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = "Confirm App Deletion") },
        text = {
            Text(text = "Are you sure you want to remove this app from the list? This action is irreversible, and you'll need to manually add the app again if needed.")
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onConfirm()
            }) {
                Text(text = stringResource(resource = Res.string.delete))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(resource = Res.string.cancel))
            }
        }
    )
}

@Composable
private fun AddNewApp(
    modifier: Modifier = Modifier,
    app: App,
    onDismiss: () -> Unit,
    onAddNewApp: (App) -> Unit
) {
    var newApp by remember {
        mutableStateOf(app)
    }
    AlertDialog(
        modifier = modifier,
        title = { Text(text = stringResource(resource = Res.string.add_new_app)) },
        text = {
            OptionChecks(app = app) {
                newApp = it
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val newApps = if (newApp.appControlButtons.isEmpty()) {
                    newApp.copy(appControlButtons = getButtonControls())
                } else {
                    newApp
                }
                onAddNewApp(newApps)
            }) {
                Text(text = stringResource(resource = Res.string.add))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(resource = Res.string.cancel))
            }
        }
    )
}

@Composable
private fun OptionChecks(
    modifier: Modifier = Modifier,
    app: App,
    onCheckedChange: (App) -> Unit,
) {
    var environmentChecked by remember { mutableStateOf(false) }
    var configurationChecked by remember { mutableStateOf(false) }
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = stringResource(resource = Res.string.add_new_app_desc))
        TextCheckBox(
            label = stringResource(resource = Res.string.launch),
            checked = true,
        )

        TextCheckBox(
            label = stringResource(resource = Res.string.environment),
            checked = environmentChecked,
            onCheckedChange = {
                environmentChecked = it
                onCheckedChange(
                    app.copy(
                        appControlButtons = getButtonControls(
                            environmentChecked,
                            configurationChecked
                        )
                    )
                )
            }
        )

        TextCheckBox(
            label = stringResource(resource = Res.string.configuration),
            checked = configurationChecked,
            onCheckedChange = {
                configurationChecked = it
                onCheckedChange(
                    app.copy(
                        appControlButtons = getButtonControls(
                            environmentChecked,
                            configurationChecked
                        )
                    )
                )
            }
        )
    }
}


fun getButtonControls(
    environmentChecked: Boolean = false,
    configurationChecked: Boolean = false
): List<AppControlButton> {
    val launch = AppControlButton(text = "Launch", action = "")
    val environment = AppControlButton(text = "Environment", action = "environment")
    val config = AppControlButton(text = "Config", action = "config")

    // Create a mutable list and always add the launch button first
    val buttonControls = mutableListOf(launch)

    // Conditionally add buttons based on the checked flags
    if (environmentChecked) buttonControls.add(environment)
    if (configurationChecked) buttonControls.add(config)

    return buttonControls
}


@Composable
private fun TextCheckBox(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview
@Composable
fun AppCardListPrev() {
    OacTheme {
        AppCardList(appDetails = emptyList(), onClick = {}) {

        }
    }
}