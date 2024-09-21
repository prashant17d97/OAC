package org.prashant.oac.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppPackages(
    @SerialName("apps")
    val apps: List<App> = emptyList()
)