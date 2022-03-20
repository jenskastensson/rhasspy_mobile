package org.rhasspy.mobile.services.mqtt

/**
 * topics where the mqtt client will publish to
 */
enum class MQTTTopicsPublish(val topic: String) {

    SessionStarted("hermes/dialogueManager/sessionStarted"),
    SessionEnded("hermes/dialogueManager/sessionEnded"),
    ContinueSession("hermes/dialogueManager/continueSession"),
    IntentNotRecognizedInSession("hermes/dialogueManager/intentNotRecognized"),
    AsrStartListening( "hermes/asr/startListening"),
    AsrStopListening("hermes/asr/stopListening"),
    AudioCaptured("rhasspy/asr/<siteId>/<sessionId>/audioCaptured"),
    AsrAudioFrame("hermes/audioServer/<siteId>/audioFrame"),
    AsrAudioSessionFrame("hermes/audioServer/<siteId>/<sessionId>/audioSessionFrame"),
    HotWordDetected("hermes/hotword/default/detected"),
    HotWordError("hermes/error/hotword"),
    Query("hermes/nlu/query"),
    IntentRecognition("hermes/nlu/query"),
    Say("hermes/tts/say"),
    AudioOutputPlayBytes("hermes/audioServer/<siteId>/playBytes/<requestId>"),
    AudioOutputPlayFinished("hermes/audioServer/<siteId>/playFinished");
}