package org.prashant.oac.util

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.app_icon
import org.jetbrains.compose.resources.painterResource


@Composable
fun DrawableImage(imageBitmap: ImageBitmap?, modifier: Modifier = Modifier) {
    if (imageBitmap == null) {
        Image(
            painter = painterResource(resource = Res.drawable.app_icon),
            contentDescription = "AppIcon",
            modifier = modifier
        )
        return
    }
    Image(
        bitmap = imageBitmap,
        contentDescription = "AppIcon",
        modifier = modifier
    )
}
