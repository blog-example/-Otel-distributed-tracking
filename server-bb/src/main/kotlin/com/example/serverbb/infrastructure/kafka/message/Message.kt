package com.example.serverbb.infrastructure.kafka.message

import java.time.Instant

data class Message<T>(
    val messageId: String,
    val correlationId: String,
    val timestamp: Instant,
    val source: String,
    val payload: T
)
