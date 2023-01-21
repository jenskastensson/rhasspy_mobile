package org.rhasspy.mobile.services.localaudio

import org.koin.core.component.inject
import org.rhasspy.mobile.MR
import org.rhasspy.mobile.fileutils.FolderType
import org.rhasspy.mobile.logger.LogType
import org.rhasspy.mobile.middleware.ServiceState
import org.rhasspy.mobile.nativeutils.AudioPlayer
import org.rhasspy.mobile.services.IService
import org.rhasspy.mobile.settings.AppSetting
import org.rhasspy.mobile.settings.sounds.SoundOption
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocalAudioService : IService() {
    private val logger = LogType.LocalAudioService.logger()

    private val params by inject<LocalAudioServiceParams>()

    private val audioPlayer = AudioPlayer()
    val isPlayingState = audioPlayer.isPlayingState

    override fun onClose() {
        logger.d { "onClose" }
        audioPlayer.stop()
        audioPlayer.close()
    }

    suspend fun playAudio(byteArray: ByteArray): ServiceState = suspendCoroutine { continuation ->
        if (AppSetting.isAudioOutputEnabled.value) {
            logger.d { "playAudio ${byteArray.size}" }
            audioPlayer.playData(
                data = byteArray,
                volume = AppSetting.volume.value,
                audioOutputOption = params.audioOutputOption,
                onFinished = {
                    logger.d { "onFinished" }
                    continuation.resume(ServiceState.Success)
                },
                onError = { exception ->
                    exception?.also {
                        logger.e(it) { "onError" }
                    } ?: run {
                        logger.e { "onError" }
                    }
                    continuation.resume(ServiceState.Exception(exception))
                }
            )
        } else {
            continuation.resume(ServiceState.Success)
        }
    }

    fun playWakeSound(onFinished: () -> Unit) {
        logger.d { "playWakeSound" }
        when (AppSetting.wakeSound.value) {
            SoundOption.Disabled.name -> {
                onFinished()
            }
            SoundOption.Default.name -> audioPlayer.playFileResource(
                MR.files.etc_wav_beep_hi,
                AppSetting.wakeSoundVolume.data,
                AppSetting.soundIndicationOutputOption.value,
                onFinished
            ) { onFinished() }
            else -> audioPlayer.playSoundFile(
                "${FolderType.SoundFolder.Wake}/${AppSetting.wakeSound.value}",
                AppSetting.wakeSoundVolume.data,
                AppSetting.soundIndicationOutputOption.value,
                onFinished
            ) { onFinished() }
        }
    }

    fun playRecordedSound() {
        logger.d { "playRecordedSound" }
        when (AppSetting.recordedSound.value) {
            SoundOption.Disabled.name -> {}
            SoundOption.Default.name -> audioPlayer.playFileResource(
                MR.files.etc_wav_beep_lo,
                AppSetting.recordedSoundVolume.data,
                AppSetting.soundIndicationOutputOption.value
            )
            else -> audioPlayer.playSoundFile(
                "${FolderType.SoundFolder.Recorded}/${AppSetting.recordedSound.value}",
                AppSetting.recordedSoundVolume.data,
                AppSetting.soundIndicationOutputOption.value
            )
        }
    }

    fun playErrorSound() {
        logger.d { "playErrorSound" }
        when (AppSetting.errorSound.value) {
            SoundOption.Disabled.name -> {}
            SoundOption.Default.name -> audioPlayer.playFileResource(
                MR.files.etc_wav_beep_error,
                AppSetting.errorSoundVolume.data,
                AppSetting.soundIndicationOutputOption.value
            )
            else -> audioPlayer.playSoundFile(
                "${FolderType.SoundFolder.Error}/${AppSetting.errorSound.value}",
                AppSetting.errorSoundVolume.data,
                AppSetting.soundIndicationOutputOption.value
            )
        }
    }

    fun stop() {
        logger.d { "stop" }
        audioPlayer.stop()
    }

}