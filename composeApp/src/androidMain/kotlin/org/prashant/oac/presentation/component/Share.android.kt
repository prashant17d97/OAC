package org.prashant.oac.presentation.component

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.prashant.oac.domain.model.AppDetails


@Composable
actual fun Share(appDetail: AppDetails, onDelete: () -> Unit) {
    val text = """
        App Name: ${appDetail.name}
        Version: ${appDetail.version}
        Version Code: ${appDetail.versionCode}
        Debuggable: ${stringResource(resource = appDetail.debuggable)}
        Released Type: ${appDetail.releaseType.key}
        Script Link: ${appDetail.deepLinkScript.joinToString(", ")}
    """.trimIndent()

    val context = LocalContext.current
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.End
        )
    ) {
        Button(onClick = {
            context.startActivity(shareIntent)
        }) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
            /*Text(stringResource(resource = Res.string.share), modifier = Modifier.padding(start = 8.dp))*/
        }

        Surface(
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.extraLarge,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {

            Icon(
                imageVector = Icons.Rounded.Delete, contentDescription = "Delete",
                modifier = Modifier.clickable(onClick = onDelete).size(40.dp).padding(4.dp)
            )
        }
    }
}