package org.rhasspy.mobile.logic.services.mqtt

/** Provides all MQTT connection options. */
data class MqttServiceConnectionOptions(
    /**
     * When set to *true* the session isn't retained. This means no subscriptions or undelivered messages are
     * stored.
     */
    val cleanSession: Boolean = false,
    val cleanStart: Boolean = true,
    val isSSLEnabled: Boolean,
    val keyStoreFile: String,
    /** Connection timeout in seconds. */
    val connectionTimeout: Int,
    /** Keep alive interval in seconds. */
    val keepAliveInterval: Int,
    val connUsername: String,
    val connPassword: String
)