package org.prashant.oac.presentation.component

import androidx.compose.runtime.Composable
import org.prashant.oac.domain.model.AppDetails

@Composable
expect fun Share(
    appDetail: AppDetails,
    onDelete: () -> Unit = {}
)