package org.rhasspy.mobile.viewmodel.screens

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.rhasspy.mobile.middleware.ServiceState
import org.rhasspy.mobile.readOnly
import org.rhasspy.mobile.services.mqtt.MqttService
import org.rhasspy.mobile.settings.ConfigurationSettings

class ConfigurationScreenViewModel : ViewModel(), KoinComponent {

    val siteId = ConfigurationSettings.siteId.data

    val isHttpServerEnabled = ConfigurationSettings.isHttpServerEnabled.data
    val isHttpServerHasError = MutableStateFlow(ServiceState.Loading).readOnly //TODO

    val isHttpSSLVerificationEnabled = ConfigurationSettings.isHttpClientSSLVerificationDisabled.data
    val isHttpClientHasError = MutableStateFlow(ServiceState.Error()).readOnly //TODO

    val isMQTTConnected get() = get<MqttService>().isConnected
    val isMqttHasError = MutableStateFlow(ServiceState.Pending).readOnly //TODO

    val wakeWordOption = ConfigurationSettings.wakeWordOption.data
    val isWakeWordServiceHasError = MutableStateFlow(ServiceState.Success()).readOnly //TODO

    val speechToTextOption = ConfigurationSettings.speechToTextOption.data
    val isSpeechToTextHasError = MutableStateFlow(ServiceState.Warning()).readOnly //TODO

    val intentRecognitionOption = ConfigurationSettings.intentRecognitionOption.data
    val isIntentRecognitionHasError = MutableStateFlow(ServiceState.Disabled).readOnly //TODO

    val textToSpeechOption = ConfigurationSettings.textToSpeechOption.data
    val isTextToSpeechHasError = MutableStateFlow(ServiceState.Loading).readOnly //TODO

    val audioPlayingOption = ConfigurationSettings.audioPlayingOption.data
    val isAudioPlayingHasError = MutableStateFlow(ServiceState.Loading).readOnly //TODO

    val dialogManagementOption = ConfigurationSettings.dialogManagementOption.data
    val isDialogManagementHasError = MutableStateFlow(ServiceState.Loading).readOnly //TODO

    val intentHandlingOption = ConfigurationSettings.intentHandlingOption.data
    val isIntentHandlingHasError = MutableStateFlow(ServiceState.Loading).readOnly //TODO

    val firstErrorIndex = MutableStateFlow(3).readOnly

    fun changeSiteId(siteId: String) {
        ConfigurationSettings.siteId.value = siteId
    }

    init {
        println("init")
    }

}