package com.example.serverb.infrastructure.kafka.producer

import com.example.serverb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverb.infrastructure.kafka.message.Message
import reactor.core.publisher.Mono

interface AirQualityProducer {
    fun send(message: Message<AirQualityPayload>): Mono<Unit>
}
