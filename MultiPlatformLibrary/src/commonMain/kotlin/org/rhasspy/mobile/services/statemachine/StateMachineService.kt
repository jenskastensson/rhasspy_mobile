package org.rhasspy.mobile.services.statemachine

import co.touchlab.kermit.Logger
import org.koin.core.component.inject
import org.rhasspy.mobile.mqtt.MqttError
import org.rhasspy.mobile.services.IService
import org.rhasspy.mobile.services.ServiceResponse
import org.rhasspy.mobile.services.hotword.HotWordServiceError
import org.rhasspy.mobile.settings.AppSettings

class StateMachineService: IService() {

    val logger = Logger.withTag("StateMachineService")
    private val params by inject<StateMachineServiceParams>()

    init {
        logger.e { params.test }
    }

    override fun onClose() {

    }

    fun startMqttSession() {

    }

    fun endMqttSession(sessionId: String?) {

    }

    fun startedMqttSession(sessionId: String?) {

    }

    fun sessionEndedMqtt(sessionId: String?) {

    }

    fun toggleHotWordEnabledMqtt(isEnabled: Boolean) {

    }

    fun startListeningMqtt(sessionId: String?, isSendAudioCaptured: Boolean) {

    }

    fun stopListeningMqtt(sessionId: String?) {

    }

    fun intentTranscribedMqtt(sessionId: String?, text: String?) {

    }

    fun intentTranscribedErrorMqtt(sessionId: String?) {

    }

    fun intentNotRecognizedMqtt(sessionId: String?) {

    }

    fun intentRecognizedMqtt(sessionId: String?, intentName: String?, intent: String) {

    }

    fun toggleIntentHandlingEnabledMqtt(isEnabled: Boolean) {

    }

    fun playAudioMqtt(data: List<Byte>) {

    }

    fun toggleAudioOutputEnabledMqtt(isEnabled: Boolean) {
        AppSettings.isAudioOutputEnabled.value = isEnabled
    }

    fun setAudioVolumeMqtt(value: Float) {

    }

    fun speechToTextResponseHttp(response: String) {
        TODO("Not yet implemented")
    }

    fun recognizeIntentResponseHttp(data: String?) {

    }

    fun textToSpeechResponseHttp(response: List<Byte>) {

    }

    fun playWavResponseHttp(response: String) {

    }

    fun intentHandlingResponseHttp(response: String) {

    }

    fun hassEventResponseHttp(response: String) {

    }

    fun hassIntentResponseHttp(response: String) {

    }

    fun listenForCommandWebServer() {

    }

    fun toggleListenForWakeWebServer(it: Boolean) {
        TODO("Not yet implemented")
    }

    fun playRecordingPostWebServer() {
        TODO("Not yet implemented")
    }

    fun playRecordingGetWebServer(): List<Byte> {
        TODO()
    }

    fun playWavWebServer(toList: List<Byte>) {
        TODO("Not yet implemented")
    }

    fun setVolumeWebServer(volume: Float?) {
        TODO("Not yet implemented")
    }

    fun startRecordingWebServer() {

    }

    fun stopRecordingWebServer() {

    }

    fun sayWebServer(receive: ByteArray) {

    }

    fun hotWordServiceError(error: HotWordServiceError) {
        TODO("Not yet implemented")
    }

    fun hotWordDetected(index: Int) {


    }

    fun mqttServiceError(error: MqttError) {


    }


    fun mqttServiceStartedSuccessfully() {


    }

    fun hotWordServiceStartedSuccessfully() {

    }

    fun silenceDetected() {
        TODO("Not yet implemented")
    }

    fun audioFrame(byteData: List<Byte>) {

    }

    fun audioFrameWakeWord(byteData: Any) {
        TODO("Not yet implemented")
    }
}