package org.prashant.oac.presentation

import OacTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.prashant.oac.MainVM
import org.prashant.oac.domain.model.AppDetails
import org.prashant.oac.presentation.component.AppCardList
import org.prashant.oac.util.AppPackageUtils.launchApp


@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    appDetails: List<AppDetails>,
    mainVM: MainVM = koinInject()
) {
    val clipboardManager = LocalClipboardManager.current

    var launchApp by remember { mutableStateOf("") }

    if (launchApp.isNotEmpty()) {
        print("LaunchApp: $launchApp")
        launchApp(launchApp)
        launchApp = ""
    }

    AppCardList(
        modifier = modifier.fillMaxSize(),
        appDetails = appDetails,
        onClick = { launchApp = it },
        onCopyClick = { clipboardManager.setText(AnnotatedString(it)) },
        onDelete = mainVM::deleteApp
    )


}


@Preview
@Composable
fun GreetingPreview() {
    OacTheme {
        HomePage(appDetails = emptyList())
    }
}