package org.prashant.oac.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppButtons(
    @SerialName("action")
    val action: String,
    @SerialName("text")
    val text: String,
    @SerialName("scriptLink")
    val scriptLink: String
)