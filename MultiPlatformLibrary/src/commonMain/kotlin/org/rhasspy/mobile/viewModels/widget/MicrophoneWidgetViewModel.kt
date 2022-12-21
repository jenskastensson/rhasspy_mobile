package org.rhasspy.mobile.viewModels.widget

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.rhasspy.mobile.*
import org.rhasspy.mobile.middleware.IServiceMiddleware
import org.rhasspy.mobile.nativeutils.MicrophonePermission
import org.rhasspy.mobile.services.dialogManager.DialogManagerServiceState
import org.rhasspy.mobile.services.dialogManager.IDialogManagerService

class MicrophoneWidgetViewModel : ViewModel(), KoinComponent {

    private val dialogManagerServiceState
        get() = getSafe<IDialogManagerService>()?.currentDialogState ?: MutableStateFlow(
            DialogManagerServiceState.Idle
        ).readOnly
    val isShowBorder =
        MutableStateFlow(true) // dialogManagerServiceState.mapReadonlyState { it == DialogManagerServiceState.AwaitingHotWord }
    val isShowMicOn: StateFlow<Boolean> = MicrophonePermission.granted
    val isRecording =
        MutableStateFlow(false) // dialogManagerServiceState.mapReadonlyState { it == DialogManagerServiceState.RecordingIntent }
    val isActionEnabled = dialogManagerServiceState
        .mapReadonlyState { it == DialogManagerServiceState.Idle || it == DialogManagerServiceState.AwaitingHotWord }

    init {
        viewModelScope.launch {
            combineState(isShowBorder, isShowMicOn, isRecording, isActionEnabled) { _, _, _, _ ->
                viewModelScope.launch {
                    Application.Instance.updateWidgetNative()
                }
            }.collect {

            }
        }
    }

    fun onTapWidget() {
        getSafe<IServiceMiddleware>()?.toggleSessionManually()
    }

}