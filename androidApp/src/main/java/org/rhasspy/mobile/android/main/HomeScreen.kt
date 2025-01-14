package org.rhasspy.mobile.android.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.get
import org.rhasspy.mobile.MR
import org.rhasspy.mobile.android.content.elements.Text
import org.rhasspy.mobile.android.content.list.FilledTonalButtonListItem
import org.rhasspy.mobile.android.permissions.RequiresMicrophonePermission
import org.rhasspy.mobile.viewmodel.element.MicrophoneFabViewModel
import org.rhasspy.mobile.viewmodel.screens.HomeScreenViewModel

/**
 * Home Screen contains
 *
 * Title bar (app name)
 * Service status information
 * wake up button
 * play recording button
 */
@Preview
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = get()) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(MR.strings.appName) }
            )
        },
    ) { paddingValues ->

        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                PortraitContent(paddingValues, viewModel)
            }

            else -> {
                LandscapeContent(paddingValues, viewModel)
            }
        }

    }

}

/**
 * Contains Column with
 *
 * Service status information
 * wake up button
 * play recording button
 */
@Composable
private fun PortraitContent(
    paddingValues: PaddingValues,
    viewModel: HomeScreenViewModel
) {

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        Box(modifier = Modifier.weight(1f)) {

            val microphoneViewModel = get<MicrophoneFabViewModel>()

            RequiresMicrophonePermission(
                MR.strings.microphonePermissionInfoRecord,
                microphoneViewModel::onClick
            ) { onClick ->
                SideEffect {
                    if (viewModel.isStartRecordingAction) {
                        viewModel.consumedStartRecordingAction()
                        onClick()
                    }
                }

                MicrophoneFab(
                    modifier = Modifier.fillMaxSize(),
                    iconSize = 96.dp,
                    viewModel = microphoneViewModel,
                    onClick = onClick
                )
            }
        }

        PlayRecording(viewModel)

    }

}

/**
 * Contains Row and Columns with
 *
 * Service status information
 * wake up button
 * play recording button
 */
@Composable
fun LandscapeContent(
    paddingValues: PaddingValues,
    viewModel: HomeScreenViewModel
) {

    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {

            val microphoneViewModel = get<MicrophoneFabViewModel>()

            RequiresMicrophonePermission(
                MR.strings.microphonePermissionInfoRecord,
                microphoneViewModel::onClick
            ) { onClick ->
                SideEffect {
                    if (viewModel.isStartRecordingAction) {
                        viewModel.consumedStartRecordingAction()
                        onClick()
                    }
                }

                MicrophoneFab(
                    modifier = Modifier.fillMaxSize(),
                    iconSize = 96.dp,
                    viewModel = microphoneViewModel,
                    onClick = onClick
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PlayRecording(viewModel)
        }
    }

}


/**
 * button to play latest recording
 */
@Composable
private fun PlayRecording(viewModel: HomeScreenViewModel) {

    val isPlaying by viewModel.isPlayingRecording.collectAsState()

    FilledTonalButtonListItem(
        onClick = viewModel::togglePlayRecording,
        enabled = viewModel.isPlayingRecordingEnabled.collectAsState().value,
        icon = if (isPlaying) Icons.Filled.Stop else Icons.Filled.PlayArrow,
        text = if (isPlaying) MR.strings.stopPlayRecording else MR.strings.playRecording
    )

}