package org.rhasspy.mobile.viewmodel.screens

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.rhasspy.mobile.logic.combineStateFlow
import org.rhasspy.mobile.logic.middleware.ServiceState
import org.rhasspy.mobile.logic.services.audioplaying.AudioPlayingService
import org.rhasspy.mobile.logic.services.dialog.DialogManagerService
import org.rhasspy.mobile.logic.services.httpclient.HttpClientService
import org.rhasspy.mobile.logic.services.intenthandling.IntentHandlingService
import org.rhasspy.mobile.logic.services.intentrecognition.IntentRecognitionService
import org.rhasspy.mobile.logic.services.mqtt.MqttService
import org.rhasspy.mobile.logic.services.speechtotext.SpeechToTextService
import org.rhasspy.mobile.logic.services.texttospeech.TextToSpeechService
import org.rhasspy.mobile.logic.services.wakeword.WakeWordService
import org.rhasspy.mobile.logic.services.webserver.WebServerService
import org.rhasspy.mobile.logic.settings.ConfigurationSetting

class ConfigurationScreenViewModel : ViewModel(), KoinComponent {

    val siteId = ConfigurationSetting.siteId.data

    val isHttpServerEnabled = ConfigurationSetting.isHttpServerEnabled.data
    val httpServerServiceState get() = get<WebServerService>().serviceState

    val isHttpSSLVerificationEnabled = ConfigurationSetting.isHttpClientSSLVerificationDisabled.data
    val httpClientServiceState get() = get<HttpClientService>().serviceState

    val isMQTTConnected get() = get<MqttService>().isConnected
    val mqttServiceState get() = get<MqttService>().serviceState

    val wakeWordOption = ConfigurationSetting.wakeWordOption.data
    val wakeWordServiceState get() = get<WakeWordService>().serviceState

    val speechToTextOption = ConfigurationSetting.speechToTextOption.data
    val speechToTextServiceState get() = get<SpeechToTextService>().serviceState

    val intentRecognitionOption = ConfigurationSetting.intentRecognitionOption.data
    val intentRecognitionServiceState get() = get<IntentRecognitionService>().serviceState

    val textToSpeechOption = ConfigurationSetting.textToSpeechOption.data
    val textToSpeechServiceState get() = get<TextToSpeechService>().serviceState

    val audioPlayingOption = ConfigurationSetting.audioPlayingOption.data
    val audioPlayingServiceState get() = get<AudioPlayingService>().serviceState

    val dialogManagementOption = ConfigurationSetting.dialogManagementOption.data
    val dialogManagementServiceState get() = get<DialogManagerService>().serviceState

    val intentHandlingOption = ConfigurationSetting.intentHandlingOption.data
    val intentHandlingServiceState get() = get<IntentHandlingService>().serviceState

    val firstErrorIndex
        get() = combineStateFlow(
            httpServerServiceState,
            httpClientServiceState,
            mqttServiceState,
            wakeWordServiceState,
            speechToTextServiceState,
            intentRecognitionServiceState,
            textToSpeechServiceState,
            audioPlayingServiceState,
            dialogManagementServiceState,
            intentHandlingServiceState
        ) { array ->
            array.indexOfFirst { it is ServiceState.Error }
        }

    val hasError
        get() = combineStateFlow(
            httpServerServiceState,
            httpClientServiceState,
            mqttServiceState,
            wakeWordServiceState,
            speechToTextServiceState,
            intentRecognitionServiceState,
            textToSpeechServiceState,
            audioPlayingServiceState,
            dialogManagementServiceState,
            intentHandlingServiceState
        ) { array ->
            array.firstOrNull { it is ServiceState.Error } != null
        }

    fun changeSiteId(siteId: String) {
        ConfigurationSetting.siteId.value = siteId
    }

    init {
        println("init")
    }

}