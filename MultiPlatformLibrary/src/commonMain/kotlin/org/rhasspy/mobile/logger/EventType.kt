package org.rhasspy.mobile.logger

enum class EventType {
    WebServerStart,
    WebServerIncomingCall,
    HttpClientStart,
    HttpClientSpeechToText,
    HttpClientRecognizeIntent,
    HttpClientTextToSpeech,
    HttpClientPlayWav,
    HttpClientIntentHandling,
    HttpClientHassEvent,
    HttpClientHassIntent,
    MqttClientStart,
    MqttClientConnecting,
    MqttClientDisconnected,
    MqttClientReconnect,
    MqttClientSubscribing,
    MqttClientPublish,
    MqttClientReceived,
    RhasspyActionRecognizeIntent,
    RhasspyActionSay,
    RhasspyActionPlayAudio,
    RhasspyActionSpeechToText,
    RhasspyActionIntentHandling,

}