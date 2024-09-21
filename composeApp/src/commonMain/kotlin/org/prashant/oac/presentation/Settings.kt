package org.prashant.oac.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.prashant.oac.domain.model.ColorContrast
import org.prashant.oac.domain.model.ThemeMode
import org.prashant.oac.domain.repositories.DataStoreRepository
import org.prashant.oac.util.PlatformUtils

@Composable
fun Settings() {

    val dataStoreRepository: DataStoreRepository = koinInject()
    val appearance by dataStoreRepository.appAppearance.collectAsState()
    val scope = rememberCoroutineScope()

    var isColorContrastExpanded by remember { mutableStateOf(false) }
    var isThemeModeExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        dataStoreRepository.fetchAppearance()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        item {
            SettingBar(
                label = "Add more app",
                onClick = {},
            )
        }
        item {
            SettingBar(label = "Change Color Contrast",
                showDropDown = true,
                isExpanded = isColorContrastExpanded,
                onClick = {
                    isColorContrastExpanded = it
                },
                dropDownContent = {
                    ColorContrast.entries.forEach {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = it.label,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        dataStoreRepository.saveAppAppearance(
                                            appAppearance = appearance.copy(
                                                colorContrast = it
                                            )
                                        )
                                        isColorContrastExpanded = false
                                    }
                                })

                            AnimatedVisibility(visible = appearance.colorContrast == it) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "Checked"
                                )
                            }
                        }
                    }
                })
        }
        item {
            SettingBar(label = "Theme",
                showDropDown = true,
                isExpanded = isThemeModeExpanded,
                onClick = {
                    isThemeModeExpanded = it
                },
                dropDownContent = {
                    ThemeMode.entries.forEach {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = it.label,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        dataStoreRepository.saveAppAppearance(
                                            appAppearance = appearance.copy(
                                                themeMode = it
                                            )
                                        )
                                        isThemeModeExpanded = false
                                    }
                                })

                            AnimatedVisibility(visible = appearance.themeMode == it) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "Checked"
                                )
                            }
                        }
                    }
                })
        }

        if (PlatformUtils.isDynamicColorSupported) {
            item {
                SettingBar(label = "Use System Palette", onClick = {}, trailContent = {
                    Switch(appearance.useSystemPalette, onCheckedChange = {
                        scope.launch {
                            dataStoreRepository.saveAppAppearance(
                                appearance.copy(useSystemPalette = it)
                            )
                        }
                    })
                })
            }
        }
    }
}


@Composable
private fun SettingBar(
    modifier: Modifier = Modifier,
    showDropDown: Boolean = false,
    label: String,
    isExpanded: Boolean = false,
    onClick: (Boolean) -> Unit,
    trailContent: @Composable () -> Unit = {
        Icon(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = "Right Arrow",
            modifier = Modifier.rotate(0f.takeIf { !isExpanded } ?: 90f))
    },
    dropDownContent: @Composable () -> Unit = {}
) {
    val updatedModifier by rememberUpdatedState(
        newValue = if (!showDropDown) {
            modifier.height(60.dp)
        } else {
            modifier // retain the original height
        }
    )
    Surface(
        modifier = Modifier.padding(4.dp),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = updatedModifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp, alignment = Alignment.CenterVertically
            ),
        ) {
            Row(
                modifier = modifier.fillMaxWidth().clickable {
                        onClick(!isExpanded)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                )

                trailContent()
            }
            AnimatedVisibility(
                visible = showDropDown && isExpanded,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    dropDownContent()
                }
            }
        }
    }
}