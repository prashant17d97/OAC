package org.prashant.oac.presentation

import OacTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.search_app
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.prashant.oac.MainVM
import org.prashant.oac.domain.model.App
import org.prashant.oac.domain.model.AppDetails
import org.prashant.oac.presentation.component.AppCardList
import org.prashant.oac.util.AppPackageUtils.getAppDetails
import org.prashant.oac.util.AppPackageUtils.launchApp
import org.prashant.oac.util.DrawableImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    modifier: Modifier = Modifier,
    mainVM: MainVM = koinInject()
) {
    var text by rememberSaveable { mutableStateOf("") }
    var newPackage by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val appDetails by mainVM.apps.collectAsState()

    var launchApp by remember { mutableStateOf("") }

    if (launchApp.isNotEmpty()) {
        launchApp(launchApp)
        launchApp = ""
    }


    var dockList: List<AppDetails> by remember {
        mutableStateOf(
            emptyList()
        )
    }

    LaunchedEffect(Unit) {
        appDetails.firstOrNull()?.let {
            dockList = listOf(it)
        }
    }
    var searchResult: List<AppDetails> by remember {
        mutableStateOf(
            emptyList()
        )
    }

    LaunchedEffect(appDetails) {
        val list = appDetails.findApp(newPackage)
        if (list.isNotEmpty()) {
            searchResult = list
        }

    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(Modifier
            .semantics { isTraversalGroup = true }
            .zIndex(1f)
            .fillMaxWidth()) {
            DockedSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                query = text,
                onQueryChange = {
                    text = it
                    dockList = appDetails.findApp(it)
                },
                onSearch = {
                    active = false
                    text = it
                    searchResult = appDetails.findApp(it).ifEmpty {
                        dockList.findApp(it)
                    }
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(stringResource(resource = Res.string.search_app)) },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = active,
                        enter = slideInHorizontally { it },
                        exit = slideOutHorizontally { it }) {
                        IconButton(onClick = {
                            searchResult = appDetails.findApp()
                            active = false
                            text = ""
                        }) {
                            Icon(Icons.Rounded.Close, contentDescription = null)
                        }
                    }
                },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(dockList) { app ->
                        SuggestionApp(
                            name = app.name,
                            icon = app.icon
                        ) {
                            text = app.name
                            searchResult = appDetails.findApp(app.name).ifEmpty {
                                dockList.findApp(app.packageName)
                            }
                        }
                    }
                }
            }
        }

        AppCardList(
            modifier = Modifier.padding(top = SearchBarDefaults.InputFieldHeight + 16.dp),
            appDetails = searchResult,
            onClick = { launchApp = it },
            onCopyClick = { clipboardManager.setText(AnnotatedString(it)) },
            onAddClick = {
                newPackage = it.packageName
                mainVM.saveNewApp(it)
            },
            onDelete = mainVM::deleteApp
        )
    }
}

@Composable
private fun SuggestionApp(
    modifier: Modifier = Modifier,
    icon: ImageBitmap?,
    name: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(
            space = 10.dp, alignment = Alignment.Start
        ), verticalAlignment = Alignment.CenterVertically
    ) {
        DrawableImage(
            imageBitmap = icon, modifier = Modifier.size(40.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}


fun List<AppDetails>.findApp(query: String = ""): List<AppDetails> {
    if (query.isEmpty()) {
        return emptyList()
    }
    val appsFromList = filter {
        it.name.contains(query, true) || it.packageName.contains(query, true)
    }
    return appsFromList.ifEmpty {
        getAppDetails(App(packageName = query))?.let { listOf(it) } ?: emptyList()
    }
}

@Preview
@Composable
private fun SearchPrev() {
    OacTheme {
        Search()
    }
}