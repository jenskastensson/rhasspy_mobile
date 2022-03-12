package org.rhasspy.mobile.services.native

import org.rhasspy.mobile.services.Action
import kotlin.native.concurrent.ThreadLocal

/**
 * Native Service to run continuously in background
 */
expect class NativeService() {

    @ThreadLocal
    companion object {

        //stores if services is currently running
        var isRunning: Boolean

        /**
         * When there is an action to be done by the services
         */
        fun doAction(action: Action)

        /**
         * stop background work
         */
        fun stop()

    }

}