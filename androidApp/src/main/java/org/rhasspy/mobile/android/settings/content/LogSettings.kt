package org.rhasspy.mobile.android.settings.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.rhasspy.mobile.MR
import org.rhasspy.mobile.android.settings.SettingsScreenItemContent
import org.rhasspy.mobile.android.utils.DropDownEnumListItem
import org.rhasspy.mobile.android.utils.SwitchListItem
import org.rhasspy.mobile.viewModels.settings.LogSettingsViewModel

/**
 * Log settings
 */
@Preview
@Composable
fun LogSettingsContent(viewModel: LogSettingsViewModel = viewModel()) {

    SettingsScreenItemContent(MR.strings.logSettings) {

        //log level
        DropDownEnumListItem(
            label = MR.strings.notConnected,
            selected = viewModel.logLevel.collectAsState().value,
            onSelect = viewModel::selectLogLevel,
            values = viewModel.logLevelOptions
        )

        //show log enabled
        SwitchListItem(
            text = MR.strings.showLog,
            isChecked = viewModel.isShowLogEnabled.collectAsState().value,
            onCheckedChange = viewModel::toggleShowLogEnabled
        )

        //audio frames logging enabled
        SwitchListItem(
            text = MR.strings.audioFramesLogging,
            isChecked = viewModel.isLogAudioFramesEnabled.collectAsState().value,
            onCheckedChange = viewModel::toggleLogAudioFramesEnabled
        )

    }

}