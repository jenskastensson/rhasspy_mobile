package org.rhasspy.mobile.nativeutils

import kotlinx.coroutines.flow.StateFlow

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MicrophonePermission {

    val granted: StateFlow<Boolean>

    fun shouldShowInformationDialog(): Boolean

    fun requestPermission(redirect: Boolean, onResult: (granted: Boolean) -> Unit)

}