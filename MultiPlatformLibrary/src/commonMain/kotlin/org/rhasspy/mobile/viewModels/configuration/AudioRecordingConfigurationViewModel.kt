package org.rhasspy.mobile.viewModels.configuration

import kotlinx.coroutines.flow.MutableStateFlow
import org.rhasspy.mobile.combineAny
import org.rhasspy.mobile.combineStateNotEquals
import org.rhasspy.mobile.readOnly
import org.rhasspy.mobile.settings.ConfigurationSettings

class AudioRecordingConfigurationViewModel : IConfigurationViewModel() {

    //unsaved data
    private val _isUdpOutputEnabled = MutableStateFlow(ConfigurationSettings.isUdpOutputEnabled.value)
    private val _udpOutputHost = MutableStateFlow(ConfigurationSettings.udpOutputHost.value)
    private val _udpOutputPort = MutableStateFlow(ConfigurationSettings.udpOutputPort.value)
    private val _udpOutputPortText = MutableStateFlow(ConfigurationSettings.udpOutputPort.value.toString())

    //unsaved ui data
    val isUdpOutputEnabled = _isUdpOutputEnabled.readOnly
    val udpOutputHost = _udpOutputHost.readOnly
    val udpOutputPort = _udpOutputPort.readOnly
    val udpOutputPortText = _udpOutputPortText.readOnly

    override val isTestingEnabled = _isUdpOutputEnabled.readOnly

    //if there are unsaved changes
    override val hasUnsavedChanges = combineAny(
        combineStateNotEquals(_isUdpOutputEnabled, ConfigurationSettings.isUdpOutputEnabled.data),
        combineStateNotEquals(_udpOutputHost, ConfigurationSettings.udpOutputHost.data),
        combineStateNotEquals(_udpOutputPort, ConfigurationSettings.udpOutputPort.data)
    )

    //show udp host and port settings
    val isOutputSettingsVisible = _isUdpOutputEnabled.readOnly

    //update if udp output is enabled
    fun toggleUdpOutputEnabled(value: Boolean) {
        _isUdpOutputEnabled.value = value
    }

    //edit udp host
    fun changeUdpOutputHost(host: String) {
        _udpOutputHost.value = host
    }

    //edit udp port
    fun changeUdpOutputPort(port: String) {
        val text = port.replace("""[-,. ]""".toRegex(), "")
        _udpOutputPortText.value = text
        _udpOutputPort.value = text.toIntOrNull() ?: 0
    }

    /**
     * save data configuration
     */
    override fun onSave() {
        ConfigurationSettings.isUdpOutputEnabled.value = _isUdpOutputEnabled.value
        ConfigurationSettings.udpOutputHost.value = _udpOutputHost.value
        ConfigurationSettings.udpOutputPort.value = _udpOutputPort.value
    }

    /**
     * undo all changes
     */
    override fun discard() {
        _isUdpOutputEnabled.value = ConfigurationSettings.isUdpOutputEnabled.value
        _udpOutputHost.value = ConfigurationSettings.udpOutputHost.value
        _udpOutputPort.value = ConfigurationSettings.udpOutputPort.value
    }

    /**
     * test unsaved data configuration
     */
    override fun onTest() {
        //only test button when enabled
        //require microphone permission
        //send data (show information that data is being send)
        TODO()
    }

}