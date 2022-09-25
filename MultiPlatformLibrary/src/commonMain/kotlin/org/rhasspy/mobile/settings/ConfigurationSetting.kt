package org.rhasspy.mobile.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import org.rhasspy.mobile.viewModels.GlobalData

/**
 * configuration settings do not change there saved value when value is changed,
 * internally unsaved value will be changed and on save it's saved to the value
 */
class ConfigurationSetting<T>(key: SettingsEnum, initial: T) : Setting<T>(key, initial) {

    init {
        GlobalData.allConfigurationSettings.add(this)
    }

    //if there are unsaved changes in this value
    var isUnsaved = false
        private set

    /**
     * holds the unsaved value
     * updated unsaved changes accordingly if the new value is different or the same as the current saved value
     */
    val unsaved = MutableStateFlow(data.value).apply {
        onEach {
            if (data.value != it) {
                //new value
                isUnsaved = true
                GlobalData.unsavedChanges.value = true
            } else {
                //set value back to saved
                isUnsaved = false
                GlobalData.updateUnsavedChanges()
            }
        }
    }

    /**
     * saves the current unsaved value and resets unsaved changes and updates the value
     */
    fun save() {
        //update unsaved changes
        isUnsaved = false
        //update saved value
        data.value = unsaved.value
    }

    /**
     * reset unsaved changes to the current value
     */
    fun reset() {
        unsaved.value = data.value
    }

}