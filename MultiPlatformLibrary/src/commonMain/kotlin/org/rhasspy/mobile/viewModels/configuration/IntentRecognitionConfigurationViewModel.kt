package org.rhasspy.mobile.viewModels.configuration

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.rhasspy.mobile.*
import org.rhasspy.mobile.data.IntentRecognitionOptions
import org.rhasspy.mobile.settings.ConfigurationSettings

class IntentRecognitionConfigurationViewModel : ViewModel() {

    //unsaved data
    private val _intentRecognitionOption = MutableStateFlow(ConfigurationSettings.intentRecognitionOption.value)
    private val _isUseCustomIntentRecognitionHttpEndpoint = MutableStateFlow(ConfigurationSettings.isUseCustomIntentRecognitionHttpEndpoint.value)
    private val _intentRecognitionHttpEndpoint = MutableStateFlow(ConfigurationSettings.intentRecognitionHttpEndpoint.value)

    //unsaved ui data
    val intentRecognitionOption = _intentRecognitionOption.readOnly
    val intentRecognitionHttpEndpoint =
        combineState(_isUseCustomIntentRecognitionHttpEndpoint, _intentRecognitionHttpEndpoint) { useCustomIntentRecognitionHttpEndpoint,
                                                                                                  intentRecognitionHttpEndpoint ->
            if (useCustomIntentRecognitionHttpEndpoint) {
                intentRecognitionHttpEndpoint
            } else {
                "${ConfigurationSettings.httpServerEndpoint.value}//api/text-to-intent"
            }
        }
    val isUseCustomIntentRecognitionHttpEndpoint = _isUseCustomIntentRecognitionHttpEndpoint.readOnly
    val isIntentRecognitionHttpEndpointChangeEnabled = isUseCustomIntentRecognitionHttpEndpoint

    val hasUnsavedChanges = combineAny(
        combineStateNotEquals(_intentRecognitionOption, ConfigurationSettings.intentRecognitionOption.data),
        combineStateNotEquals(_isUseCustomIntentRecognitionHttpEndpoint, ConfigurationSettings.isUseCustomIntentRecognitionHttpEndpoint.data),
        combineStateNotEquals(_intentRecognitionHttpEndpoint, ConfigurationSettings.intentRecognitionHttpEndpoint.data)
    )

    //show endpoint settings
    val isIntentRecognitionHttpSettingsVisible = _intentRecognitionOption.mapReadonlyState { it == IntentRecognitionOptions.RemoteHTTP }

    //all options
    val intentRecognitionOptionsList = IntentRecognitionOptions::values

    //set new intent recognition option
    fun selectIntentRecognitionOption(option: IntentRecognitionOptions) {
        _intentRecognitionOption.value = option
    }

    //toggle if custom endpoint is used
    fun toggleUseCustomHttpEndpoint(enabled: Boolean) {
        _isUseCustomIntentRecognitionHttpEndpoint.value = enabled
    }

    //set new intent recognition option
    fun changeIntentRecognitionHttpEndpoint(endpoint: String) {
        _intentRecognitionHttpEndpoint.value = endpoint
    }

    /**
     * save data configuration
     */
    fun save() {
        ConfigurationSettings.intentRecognitionOption.value = _intentRecognitionOption.value
        ConfigurationSettings.isUseCustomIntentRecognitionHttpEndpoint.value = _isUseCustomIntentRecognitionHttpEndpoint.value
        ConfigurationSettings.intentRecognitionHttpEndpoint.value = _intentRecognitionHttpEndpoint.value
    }

    fun discard() {
        _intentRecognitionOption.value = ConfigurationSettings.intentRecognitionOption.value
        _isUseCustomIntentRecognitionHttpEndpoint.value = ConfigurationSettings.isUseCustomIntentRecognitionHttpEndpoint.value
        _intentRecognitionHttpEndpoint.value = ConfigurationSettings.intentRecognitionHttpEndpoint.value
    }

    /**
     * test unsaved data configuration
     */
    fun test() {

    }

}