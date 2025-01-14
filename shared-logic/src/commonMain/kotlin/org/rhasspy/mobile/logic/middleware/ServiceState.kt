package org.rhasspy.mobile.logic.middleware

import dev.icerock.moko.resources.StringResource

sealed class ServiceState {

    object Pending : ServiceState()

    object Loading : ServiceState()

    object Success : ServiceState()

    class Exception(val exception: kotlin.Exception? = null) : ServiceState()

    class Error(val information: StringResource) : ServiceState()

    object Disabled : ServiceState()

}