package org.rhasspy.mobile.android.content.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.StringResource
import org.rhasspy.mobile.android.content.elements.Icon
import org.rhasspy.mobile.android.content.elements.Text

@Composable
fun FilledTonalButtonListItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: StringResource,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    ListElement(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                onClick = onClick,
                content = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        icon?.also {
                            Icon(
                                imageVector = icon,
                                contentDescription = text
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Text(resource = text)
                    }
                })
        }
    }
}