package org.rhasspy.mobile.logic.settings.option

import dev.icerock.moko.resources.StringResource
import org.rhasspy.mobile.MR

enum class HomeAssistantIntentHandlingOption(override val text: StringResource) : IOption<HomeAssistantIntentHandlingOption> {
    Event(MR.strings.hassEvent),
    Intent(MR.strings.intentHandling);

    override fun findValue(value: String): HomeAssistantIntentHandlingOption {
        return HomeAssistantIntentHandlingOption.valueOf(value)
    }
}