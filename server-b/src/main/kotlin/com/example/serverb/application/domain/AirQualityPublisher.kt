package com.example.serverb.application.domain

import reactor.core.publisher.Mono

interface AirQualityPublisher {

    fun publish(event: AirQualityEvent): Mono<PublishResult>

    data class AirQualityEvent(
        val longitude: Double,
        val latitude: Double,
        val pollutants: List<PollutantEvent>,
        val deviceId: String
    )

    data class PollutantEvent(
        val type: String,
        val value: Double
    )

    data class PublishResult(
        val messageId: String
    )
}
