package org.rhasspy.mobile.android.permissions

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.rhasspy.mobile.MR
import org.rhasspy.mobile.android.navigation.LocalSnackbarHostState
import org.rhasspy.mobile.android.utils.Text
import org.rhasspy.mobile.android.utils.translate
import org.rhasspy.mobile.nativeutils.MicrophonePermission
import org.rhasspy.mobile.viewModels.HomeScreenViewModel

/**
 * 3 parts where this is shown:
 *
 * click on record button
 * local WakeWord service
 * click on check audio level button
 */
@Composable
fun MicrophonePermissionRequired(viewModel: HomeScreenViewModel) {
    AnimatedVisibility(
        enter = fadeIn(animationSpec = tween(50)),
        exit = fadeOut(animationSpec = tween(50)),
        visible = !viewModel.isMicrophonePermissionRequestNotRequired.collectAsState().value
    ) {
        val microphonePermission = requestMicrophonePermission(MR.strings.microphonePermissionInfoWakeWord) {}

        IconButton(
            onClick = { microphonePermission.invoke() },
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(8.dp)
            )
        )
        {
            org.rhasspy.mobile.android.utils.Icon(
                imageVector = Icons.Filled.MicOff,
                tint = MaterialTheme.colorScheme.onErrorContainer,
                contentDescription = MR.strings.microphone
            )
        }
    }
}

@Composable
fun requestMicrophonePermission(
    informationText: StringResource,
    onResult: (granted: Boolean) -> Unit
): () -> Unit {
    val snackbarHostState = LocalSnackbarHostState.current
    val openRequestPermissionDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val activity = LocalContext.current as ComponentActivity

    val snackBarMessage = translate(MR.strings.microphonePermissionDenied)
    val snackBarActionLabel = translate(MR.strings.settings)

    if (openRequestPermissionDialog.value) {
        MicrophonePermissionInfoDialog(informationText) { result ->
            openRequestPermissionDialog.value = false
            if (result) {
                requestMicrophonePermissionFromSystem(snackBarMessage, snackBarActionLabel, coroutineScope, snackbarHostState, onResult)
            }
        }
    }

    return {
        if (!MicrophonePermission.granted.value) {
            if (MicrophonePermission.shouldShowInfoDialog(activity)) {
                openRequestPermissionDialog.value = true
            } else {
                requestMicrophonePermissionFromSystem(snackBarMessage, snackBarActionLabel, coroutineScope, snackbarHostState, onResult)
            }
        } else {
            onResult.invoke(true)
        }
    }
}


private fun requestMicrophonePermissionFromSystem(
    message: String,
    actionLabel: String,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onResult: (granted: Boolean) -> Unit
) {
    MicrophonePermission.requestPermission(false) { granted ->
        if (granted) {
            onResult.invoke(true)
        } else {
            coroutineScope.launch {

                val snackbarResult = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short,
                )

                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    MicrophonePermission.requestPermission(true) {
                        onResult.invoke(it)
                    }
                } else {
                    onResult.invoke(false)
                }
            }
        }
    }
}

@Composable
private fun MicrophonePermissionInfoDialog(message: StringResource, onResult: (result: Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { onResult.invoke(false) },
        title = { Text(MR.strings.microphonePermissionDialogTitle) },
        text = { Text(message) },
        icon = { org.rhasspy.mobile.android.utils.Icon(imageVector = Icons.Filled.Mic, contentDescription = MR.strings.microphone) },
        confirmButton = {
            Button(onClick = { onResult.invoke(true) }) {
                Text(MR.strings.ok)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onResult.invoke(false) }) {
                Text(MR.strings.cancel)
            }
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}