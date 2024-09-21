package org.prashant.oac.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class App(
    @SerialName("buttons")
    val appControlButtons: List<AppControlButton> = emptyList(),
    @SerialName("package")
    val packageName: String
)