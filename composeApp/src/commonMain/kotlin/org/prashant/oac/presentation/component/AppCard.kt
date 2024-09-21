package org.prashant.oac.presentation.component

import OacTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.add
import oac.composeapp.generated.resources.debuggable
import oac.composeapp.generated.resources.released_type
import oac.composeapp.generated.resources.version
import oac.composeapp.generated.resources.versionCode
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.prashant.oac.domain.model.AppDetails
import org.prashant.oac.util.DrawableImage

@Composable
fun AppCard(
    modifier: Modifier = Modifier, appDetail: AppDetails,
    onClick: (scriptLink: String) -> Unit,
    onCopyClick: (String) -> Unit,
    onAddClick: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Surface(
        modifier = modifier.padding(4.dp),
        tonalElevation = 4.dp, shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp, alignment = Alignment.Top
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 10.dp, alignment = Alignment.Start
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 10.dp, alignment = Alignment.Start
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    DrawableImage(
                        imageBitmap = appDetail.icon, modifier = Modifier.size(60.dp)
                    )
                    Text(
                        text = appDetail.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                if (appDetail.deepLinkScript.isNotEmpty()) {
                    Share(
                        appDetail,
                        onDelete = onDelete
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Top)
            ) {
                LabelText(
                    label = stringResource(resource = Res.string.version),
                    value = appDetail.version
                )
                LabelText(
                    label = stringResource(resource = Res.string.versionCode),
                    value = appDetail.versionCode.toString()
                )
                LabelText(
                    label = stringResource(resource = Res.string.debuggable),
                    value = stringResource(resource = appDetail.debuggable)
                )
                LabelText(
                    label = stringResource(resource = Res.string.released_type),
                    value = stringResource(appDetail.releaseType)
                )
                appDetail.deepLinkScript.forEach { script ->
                    ScriptCopy(script = script, onClick = onCopyClick)
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                appDetail.appControlButtons.forEach { buttonDetail ->
                    TextButton(
                        text = buttonDetail.text,
                        onClick = { onClick(buttonDetail.scriptLink) })
                }

                AnimatedVisibility(visible = appDetail.appControlButtons.isEmpty()) {
                    TextButton(
                        text = stringResource(Res.string.add),
                        onClick = onAddClick
                    )
                }
            }
        }

    }
}


@Preview
@Composable
private fun AppCardPrev() {
    OacTheme {
        AppCard(modifier = Modifier, appDetail = AppDetails(
            appControlButtons = listOf(),
            deepLinkScript = listOf(),
            isDebug = false,
            name = "Johnathan Salinas",
            releaseType = Res.string.released_type,
            version = "explicari",
            packageName = "explicari",
            versionCode = 9756
        ), onClick = {}, onCopyClick = {})
    }
}