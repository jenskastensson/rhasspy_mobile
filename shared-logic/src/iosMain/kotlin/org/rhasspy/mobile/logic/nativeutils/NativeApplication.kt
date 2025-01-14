package org.rhasspy.mobile.logic.nativeutils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual abstract class NativeApplication {

    actual val currentlyAppInBackground: MutableStateFlow<Boolean>
        get() = TODO("Not yet implemented")
    actual val isAppInBackground: StateFlow<Boolean>
        get() = TODO("Not yet implemented")

    actual abstract suspend fun updateWidgetNative()

    actual fun isInstrumentedTest(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun openLink(link: String) {
        TODO("Not yet implemented")
    }

    actual abstract val isHasStarted: StateFlow<Boolean>
    actual abstract suspend fun reloadServiceModules()
    actual abstract suspend fun startTest()
    actual abstract suspend fun stopTest()
    actual abstract fun setCrashlyticsCollectionEnabled(enabled: Boolean)
    actual fun restart() {
    }

    actual fun onCreate() {
    }

}