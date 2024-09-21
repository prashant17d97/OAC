package org.prashant.oac.presentation.component

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.copy
import org.jetbrains.compose.resources.painterResource

@Composable
fun LabelText(modifier: Modifier = Modifier, label: String, value: String) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
            space = 6.dp, alignment = Alignment.Start
        ), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(text = value)
    }
}

@Composable
fun ScriptCopy(modifier: Modifier = Modifier, script: String, onClick: (String) -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = script,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(
                resource = Res.drawable.copy
            ),
            contentDescription = "Copy",
            modifier = Modifier
                .size(30.dp)
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = LocalIndication.current
                ) { onClick(script) }
                .padding(2.dp)
        )

    }
}