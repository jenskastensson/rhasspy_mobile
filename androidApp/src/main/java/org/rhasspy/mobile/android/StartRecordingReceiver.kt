package org.rhasspy.mobile.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.rhasspy.mobile.logic.nativeutils.MicrophonePermission
import org.rhasspy.mobile.viewmodel.element.MicrophoneFabViewModel

/**
 * Start Recording Receiver to not launch Main Activity when Permission is given
 */
class StartRecordingReceiver : KoinComponent, BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("StartRecordingReceiver StartRecordingReceiver StartRecordingReceiver")
        if (MicrophonePermission.granted.value) {
            get<MicrophoneFabViewModel>().onClick()
        } else {
            MainActivity.startRecordingAction()
        }
    }
}